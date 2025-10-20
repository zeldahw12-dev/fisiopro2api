package com.fisio.fisio.service;

import com.fisio.fisio.dto.UsuarioDTO;
import com.fisio.fisio.dto.signup.CompleteSignupRequest;
import com.fisio.fisio.dto.signup.StartSignupRequest;
import com.fisio.fisio.dto.signup.VerifyCodeRequest;
import com.fisio.fisio.model.Usuario;
import com.fisio.fisio.model.VerificationCode;
import com.fisio.fisio.repository.UsuarioRepository;
import com.fisio.fisio.repository.VerificationCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SignupService {

    private final VerificationCodeRepository verificationRepo;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    @Value("${app.signup.code.expiration-minutes:10}")
    private int expirationMinutes;

    public SignupService(VerificationCodeRepository verificationRepo,
                         UsuarioRepository usuarioRepository,
                         EmailService emailService) {
        this.verificationRepo = verificationRepo;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    private String normalizeEmail(String raw) {
        return raw == null ? null : raw.trim().toLowerCase(Locale.ROOT);
    }

    private String generateCode() {
        int n = ThreadLocalRandom.current().nextInt(0, 1_000_000);
        return String.format("%06d", n);
    }

    /* ===================== SIGNUP EXISTENTE ===================== */

    @Transactional
    public void start(StartSignupRequest req) {
        String email = normalizeEmail(req.getEmail());

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        verificationRepo.markAllUnusedAsUsedForEmail(email);

        String code = generateCode();
        VerificationCode v = new VerificationCode(
                email,
                code,
                LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(expirationMinutes)
        );
        v.setUsed(false);
        v.setAttempts(0);
        verificationRepo.save(v);

        emailService.sendVerificationCode(email, code);
    }

    @Transactional
    public void verify(VerifyCodeRequest req) {
        String email = normalizeEmail(req.getEmail());
        String code = req.getCode() == null ? "" : req.getCode().trim();

        VerificationCode v = verificationRepo
                .findTopByEmailAndUsedFalseAndExpiresAtAfterOrderByIdDesc(
                        email, LocalDateTime.now(ZoneId.of("UTC")))
                .orElseThrow(() -> new IllegalArgumentException("Primero solicita un código"));

        if (!v.getCode().equals(code)) {
            v.setAttempts(v.getAttempts() + 1);
            verificationRepo.save(v);
            throw new IllegalArgumentException("Código incorrecto");
        }

        v.setUsed(true);
        verificationRepo.save(v);
    }

    @Transactional
    public UsuarioDTO complete(CompleteSignupRequest req) {
        String email = normalizeEmail(req.getEmail());

        verificationRepo.findTopByEmailAndUsedOrderByIdDesc(email, true)
                .orElseThrow(() -> new IllegalArgumentException("Verifica tu email antes de completar el registro"));

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (!req.getContra().equals(req.getConfirmarContra())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        Usuario u = new Usuario();
        u.setNickname(req.getNickname().trim());
        u.setNombre(req.getNombre().trim());
        u.setEdad(req.getEdad());
        u.setEmail(email);
        u.setContra(req.getContra()); // TODO: hash en producción
        u.setFoto(req.getFoto());
        u.setProfesion(req.getProfesion());

        Usuario saved = usuarioRepository.save(u);

        verificationRepo.markAllUnusedAsUsedForEmail(email);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(saved.getIdUsuario());
        dto.setNickname(saved.getNickname());
        dto.setNombre(saved.getNombre());
        dto.setEdad(saved.getEdad());
        dto.setEmail(saved.getEmail());
        dto.setContra(saved.getContra());
        dto.setFoto(saved.getFoto());
        dto.setProfesion(saved.getProfesion());
        return dto;
    }

    /* ===================== CAMBIO DE EMAIL NUEVO ===================== */

    /**
     * Paso 1: inicia cambio de correo. Envía código al NUEVO email.
     */
    @Transactional
    public void startEmailChange(Integer idUsuario, String newEmailRaw) {
        String newEmail = normalizeEmail(newEmailRaw);

        Usuario user = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (user.getEmail().equalsIgnoreCase(newEmail)) {
            throw new IllegalArgumentException("El nuevo email es igual al actual");
        }
        if (usuarioRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("El email ya está en uso por otra cuenta");
        }

        // Invalidar códigos pendientes para ese nuevo email
        verificationRepo.markAllUnusedAsUsedForEmail(newEmail);

        String code = generateCode();
        VerificationCode v = new VerificationCode(
                newEmail,
                code,
                LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(expirationMinutes)
        );
        v.setUsed(false);
        v.setAttempts(0);
        verificationRepo.save(v);

        emailService.sendEmailChangeCode(newEmail, code);
    }

    /**
     * Paso 2: verificar el código recibido en el NUEVO email.
     */
    @Transactional
    public void verifyEmailChange(String newEmailRaw, String codeRaw) {
        String newEmail = normalizeEmail(newEmailRaw);
        String code = codeRaw == null ? "" : codeRaw.trim();

        VerificationCode v = verificationRepo
                .findTopByEmailAndUsedFalseAndExpiresAtAfterOrderByIdDesc(
                        newEmail, LocalDateTime.now(ZoneId.of("UTC")))
                .orElseThrow(() -> new IllegalArgumentException("Primero solicita un código"));

        if (!v.getCode().equals(code)) {
            v.setAttempts(v.getAttempts() + 1);
            verificationRepo.save(v);
            throw new IllegalArgumentException("Código incorrecto");
        }

        v.setUsed(true);
        verificationRepo.save(v);
    }

    /**
     * Paso 3: commit del cambio. Requiere que exista un código USADO para el NUEVO email.
     */
    @Transactional
    public void commitEmailChange(Integer idUsuario, String newEmailRaw) {
        String newEmail = normalizeEmail(newEmailRaw);

        Usuario user = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Verificar que el último código para el nuevo email fue usado (validado)
        verificationRepo.findTopByEmailAndUsedOrderByIdDesc(newEmail, true)
                .orElseThrow(() -> new IllegalArgumentException("Debes validar el código enviado al nuevo correo"));

        // Último check por condición de carrera
        if (usuarioRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("El email ya está en uso por otra cuenta");
        }

        user.setEmail(newEmail);
        usuarioRepository.save(user);

        // Invalidar cualquier código pendiente asociado al nuevo email
        verificationRepo.markAllUnusedAsUsedForEmail(newEmail);

        // Aviso opcional
        emailService.sendEmailChangeSuccess(newEmail);
    }
}
