package com.fisio.fisio.service;

import com.fisio.fisio.dto.auth.EmailChangeCommitRequest;
import com.fisio.fisio.dto.auth.EmailChangeStartRequest;
import com.fisio.fisio.dto.auth.EmailChangeVerifyRequest;
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
@Transactional
public class EmailChangeService {

    private final UsuarioRepository usuarioRepository;
    private final VerificationCodeRepository verificationRepo;
    private final EmailService emailService;

    // usamos el mismo TTL que el signup
    @Value("${app.signup.code.expiration-minutes:10}")
    private int expirationMinutes;

    // intentos máximos antes de invalidar el código
    @Value("${signup.code.max-attempts:5}")
    private int maxAttempts;

    public EmailChangeService(UsuarioRepository usuarioRepository,
                              VerificationCodeRepository verificationRepo,
                              EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.verificationRepo = verificationRepo;
        this.emailService = emailService;
    }

    private String normalizeEmail(String raw) {
        return raw == null ? null : raw.trim().toLowerCase(Locale.ROOT);
    }

    private String generateCode() {
        int n = ThreadLocalRandom.current().nextInt(0, 1_000_000);
        return String.format("%06d", n);
    }

    /** Paso 1: enviar código al NUEVO correo */
    public void start(EmailChangeStartRequest req) {
        String newEmail = normalizeEmail(req.getNewEmail());

        Usuario usuario = usuarioRepository.findById(req.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // (Opcional) evitar que otro usuario ya tenga ese correo
        usuarioRepository.findByEmail(newEmail).ifPresent(u -> {
            if (!u.getIdUsuario().equals(usuario.getIdUsuario())) {
                throw new IllegalStateException("Ya existe un usuario con ese correo");
            }
        });

        String code = generateCode();
        LocalDateTime expiresAt = LocalDateTime
                .now(ZoneId.of("UTC"))
                .plusMinutes(expirationMinutes);

        VerificationCode v = new VerificationCode(newEmail, code, expiresAt);
        v.setUsed(false);
        v.setAttempts(0);
        verificationRepo.save(v);

        emailService.sendEmailChangeCode(newEmail, code);
    }

    /** Paso 2: verificar código enviado al nuevo correo */
    public void verify(EmailChangeVerifyRequest req) {
        String email = normalizeEmail(req.getNewEmail());
        String code = req.getCode().trim();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        VerificationCode v = verificationRepo
                .findTopByEmailAndUsedFalseAndExpiresAtAfterOrderByIdDesc(email, now)
                .orElseThrow(() -> new IllegalArgumentException("Primero solicita un código para ese correo"));

        if (v.getAttempts() >= maxAttempts) {
            v.setUsed(true);
            verificationRepo.save(v);
            throw new IllegalStateException("Se superó el número de intentos, solicita un nuevo código.");
        }

        if (!v.getCode().equals(code)) {
            v.setAttempts(v.getAttempts() + 1);
            verificationRepo.save(v);
            throw new IllegalArgumentException("Código incorrecto");
        }

        v.setUsed(true);
        verificationRepo.save(v);
    }

    /** Paso 3: aplicar el cambio de email al usuario */
    public void commit(EmailChangeCommitRequest req) {
        String newEmail = normalizeEmail(req.getNewEmail());

        Usuario usuario = usuarioRepository.findById(req.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        // verificamos que haya un código usado (verificado) reciente para ese correo
        VerificationCode lastUsed = verificationRepo
                .findTopByEmailAndUsedOrderByIdDesc(newEmail, true)
                .orElseThrow(() -> new IllegalStateException("Primero verifica el código para ese correo"));

        if (lastUsed.getExpiresAt().isBefore(now)) {
            throw new IllegalStateException("El código ya expiró, solicita uno nuevo.");
        }

        usuario.setEmail(newEmail);
        usuarioRepository.save(usuario);

        emailService.sendEmailChangeSuccess(newEmail);
    }
}
