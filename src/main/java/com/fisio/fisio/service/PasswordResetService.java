// package com.fisio.fisio.service;

package com.fisio.fisio.service;

import com.fisio.fisio.dto.auth.ForgotPasswordRequest;
import com.fisio.fisio.dto.auth.ResetPasswordRequest;
import com.fisio.fisio.dto.signup.VerifyCodeRequest;
import com.fisio.fisio.model.VerificationCode;
import com.fisio.fisio.model.Usuario;
import com.fisio.fisio.repository.UsuarioRepository;
import com.fisio.fisio.repository.VerificationCodeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class PasswordResetService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random = new SecureRandom();

    public PasswordResetService(
            VerificationCodeRepository verificationCodeRepository,
            UsuarioRepository usuarioRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder
    ) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // 1) Usuario pone su email → mandas código
    @Transactional
    public void startForgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        Optional<Usuario> userOpt = usuarioRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            // Opcional: no revelar si existe o no
            return;
        }

        // Invalida código anterior (si quieres)
        verificationCodeRepository.findTopByEmailAndUsedIsFalseOrderByCreatedAtDesc(email)
                .ifPresent(vc -> {
                    vc.setUsed(true);
                    verificationCodeRepository.save(vc);
                });

        String code = generateCode();
        LocalDateTime expiresAt = LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(10);

        VerificationCode vc = new VerificationCode(email, code, expiresAt);
        verificationCodeRepository.save(vc);

        // Envía correo (implementación tuya)
        emailService.sendForgotPasswordCode(email, code);
    }

    // 2) Usuario escribe el código → solo validas que sea correcto
    @Transactional
    public void verifyForgotPasswordCode(VerifyCodeRequest request) {
        VerificationCode vc = getActiveCodeOrThrow(request.getEmail());

        if (!vc.getCode().equals(request.getCode().trim())) {
            incrementAttempts(vc);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código incorrecto");
        }

        if (isExpired(vc)) {
            vc.setUsed(true);
            verificationCodeRepository.save(vc);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código ha expirado");
        }

        // Si llega aquí, el código es válido. No lo marcamos como usado todavía.
    }

    // 3) Usuario escribe nueva contraseña + confirmarla → aquí se cambia
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        VerificationCode vc = getActiveCodeOrThrow(request.getEmail());

        if (!vc.getCode().equals(request.getCode().trim())) {
            incrementAttempts(vc);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código incorrecto");
        }

        if (isExpired(vc)) {
            vc.setUsed(true);
            verificationCodeRepository.save(vc);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código ha expirado");
        }

        Usuario user = usuarioRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Ajusta el setter al nombre real de tu campo de contraseña
        user.setContra(passwordEncoder.encode(request.getNewPassword()));
        usuarioRepository.save(user);

        // Ya se usó este código
        vc.setUsed(true);
        verificationCodeRepository.save(vc);
    }

    // --- helpers privados ---

    private String generateCode() {
        int n = random.nextInt(900000) + 100000; // 6 dígitos
        return String.valueOf(n);
    }

    private VerificationCode getActiveCodeOrThrow(String emailRaw) {
        String email = emailRaw.trim().toLowerCase();

        VerificationCode vc = verificationCodeRepository
                .findTopByEmailAndUsedIsFalseOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "No hay código activo para este email"));

        return vc;
    }

    private boolean isExpired(VerificationCode vc) {
        return vc.getExpiresAt().isBefore(LocalDateTime.now(ZoneId.of("UTC")));
    }

    private void incrementAttempts(VerificationCode vc) {
        vc.setAttempts(vc.getAttempts() + 1);
        if (vc.getAttempts() >= 5) {
            vc.setUsed(true);
        }
        verificationCodeRepository.save(vc);
    }
}
