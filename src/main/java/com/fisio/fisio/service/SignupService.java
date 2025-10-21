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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SignupService {

    private final VerificationCodeRepository verificationRepo;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final PlatformTransactionManager txManager;

    @Value("${app.signup.code.expiration-minutes:10}")
    private int expirationMinutes;

    public SignupService(VerificationCodeRepository verificationRepo,
                         UsuarioRepository usuarioRepository,
                         EmailService emailService,
                         PlatformTransactionManager txManager) {
        this.verificationRepo = verificationRepo;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.txManager = txManager;
    }

    private String normalizeEmail(String raw) {
        return raw == null ? null : raw.trim().toLowerCase(Locale.ROOT);
    }

    private String generateCode() {
        int n = ThreadLocalRandom.current().nextInt(0, 1_000_000);
        return String.format("%06d", n);
    }

    /* ===================== SIGNUP ===================== */

    @Transactional
    public void start(StartSignupRequest req) {
        String email = normalizeEmail(req.getEmail());

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // NO hacer UPDATE masivo aquí. Sólo insertamos nuevo código:
        String code = generateCode();
        VerificationCode v = new VerificationCode(
                email,
                code,
                LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(expirationMinutes)
        );
        v.setUsed(false);
        v.setAttempts(0);
        v = verificationRepo.save(v);

        // Limpieza de códigos anteriores en una tx separada, rápida:
        invalidateOldAsync(email, v.getId());

        // Enviar correo fuera de cualquier tx larga
        emailService.sendVerificationCode(email, code);
    }

    @Transactional
    public void verify(VerifyCodeRequest req) {
        String email = normalizeEmail(req.getEmail());
        String code = req.getCode() == null ? "" : req.getCode().trim();

        // Bloquea SÓLO el último sin usar (fail-fast)
        var page1 = PageRequest.of(0, 1);
        List<VerificationCode> locked = verificationRepo.lockLatestUnusedByEmail(email, page1);
        if (locked.isEmpty()) {
            throw new IllegalArgumentException("Primero solicita un código");
        }

        VerificationCode latest = locked.get(0);

        // Expiración
        if (latest.getExpiresAt().isBefore(LocalDateTime.now(ZoneId.of("UTC")))) {
            // marcarlo usado para no volver a servirlo
            verificationRepo.markUsedById(latest.getId());
            throw new IllegalArgumentException("El código ha expirado");
        }

        if (!latest.getCode().equals(code)) {
            latest.setAttempts(latest.getAttempts() + 1);
            verificationRepo.save(latest); // no bloquea otras filas
            throw new IllegalArgumentException("Código incorrecto");
        }

        // Marca usado por id (1 fila)
        int updated = verificationRepo.markUsedById(latest.getId());
        if (updated != 1) {
            throw new IllegalStateException("No se pudo confirmar el código, intenta de nuevo");
        }
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
        u.setContra(req.getContra()); // TODO: hashear en producción
        u.setFoto(req.getFoto());
        u.setProfesion(req.getProfesion());

        Usuario saved = usuarioRepository.save(u);

        // Invalidar cualquier pendiente restante (no crítico, pero deja limpio)
        invalidateOldAsync(email, null);

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

    /* ===================== CAMBIO DE EMAIL ===================== */

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

        String code = generateCode();
        VerificationCode v = new VerificationCode(
                newEmail,
                code,
                LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(expirationMinutes)
        );
        v.setUsed(false);
        v.setAttempts(0);
        v = verificationRepo.save(v);

        invalidateOldAsync(newEmail, v.getId());

        emailService.sendEmailChangeCode(newEmail, code);
    }

    @Transactional
    public void verifyEmailChange(String newEmailRaw, String codeRaw) {
        String newEmail = normalizeEmail(newEmailRaw);
        String code = codeRaw == null ? "" : codeRaw.trim();

        var page1 = PageRequest.of(0, 1);
        List<VerificationCode> locked = verificationRepo.lockLatestUnusedByEmail(newEmail, page1);
        if (locked.isEmpty()) {
            throw new IllegalArgumentException("Primero solicita un código");
        }

        VerificationCode latest = locked.get(0);

        if (latest.getExpiresAt().isBefore(LocalDateTime.now(ZoneId.of("UTC")))) {
            verificationRepo.markUsedById(latest.getId());
            throw new IllegalArgumentException("El código ha expirado");
        }

        if (!latest.getCode().equals(code)) {
            latest.setAttempts(latest.getAttempts() + 1);
            verificationRepo.save(latest);
            throw new IllegalArgumentException("Código incorrecto");
        }

        int updated = verificationRepo.markUsedById(latest.getId());
        if (updated != 1) {
            throw new IllegalStateException("No se pudo confirmar el código, intenta de nuevo");
        }
    }

    @Transactional
    public void commitEmailChange(Integer idUsuario, String newEmailRaw) {
        String newEmail = normalizeEmail(newEmailRaw);

        Usuario user = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        verificationRepo.findTopByEmailAndUsedOrderByIdDesc(newEmail, true)
                .orElseThrow(() -> new IllegalArgumentException("Debes validar el código enviado al nuevo correo"));

        if (usuarioRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("El email ya está en uso por otra cuenta");
        }

        user.setEmail(newEmail);
        usuarioRepository.save(user);

        invalidateOldAsync(newEmail, null);

        emailService.sendEmailChangeSuccess(newEmail);
    }

    /* ===================== Utilidades ===================== */

    private void invalidateOldAsync(String email, Integer keepId) {
        // Transacción separada, corta, para no bloquear el flujo de /start o /complete
        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.setTimeout(5);
        try {
            tt.executeWithoutResult(s -> {
                if (keepId == null) {
                    verificationRepo.markAllUnusedAsUsedForEmail(email);
                } else {
                    verificationRepo.invalidateOthers(email, keepId);
                }
            });
        } catch (Exception ignored) {
            // No romper el flujo principal por la limpieza
        }
    }
}
