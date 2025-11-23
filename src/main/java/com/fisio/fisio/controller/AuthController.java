package com.fisio.fisio.controller;

import com.fisio.fisio.dto.UsuarioDTO;
import com.fisio.fisio.dto.signup.CompleteSignupRequest;
import com.fisio.fisio.dto.signup.StartSignupRequest;
import com.fisio.fisio.dto.signup.VerifyCodeRequest;
import com.fisio.fisio.dto.auth.ForgotPasswordRequest;
import com.fisio.fisio.dto.auth.EmailChangeStartRequest;
import com.fisio.fisio.dto.auth.EmailChangeVerifyRequest;
import com.fisio.fisio.dto.auth.EmailChangeCommitRequest;
import com.fisio.fisio.model.Usuario;
import com.fisio.fisio.repository.UsuarioRepository;
import com.fisio.fisio.service.EmailChangeService;
import com.fisio.fisio.service.EmailService;
import com.fisio.fisio.service.SignupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final SignupService signupService;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final EmailChangeService emailChangeService; // 👈 NUEVO

    public AuthController(SignupService signupService,
                          UsuarioRepository usuarioRepository,
                          EmailService emailService,
                          EmailChangeService emailChangeService) { // 👈 NUEVO
        this.signupService = signupService;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.emailChangeService = emailChangeService;      // 👈 NUEVO
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String email = normalizeEmail(req.get("email"));
        String password = req.get("password");

        Usuario usuario = usuarioRepository.findByEmail(email)
                .filter(u -> u.getContra().equals(password))
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Credenciales inválidas"));
        }

        // Aquí podrías generar un token JWT, pero por ahora devolvemos datos simples:
        return ResponseEntity.ok(Map.of(
                "token", "fake-jwt-token",
                "user", usuario
        ));
    }

    private String normalizeEmail(String raw) {
        return raw == null ? null : raw.trim().toLowerCase(Locale.ROOT);
    }

    /** Paso 1: Enviar código de verificación (signup) */
    @PostMapping("/signup/start")
    public ResponseEntity<?> start(@Valid @RequestBody StartSignupRequest req) {
        signupService.start(req);
        return ResponseEntity.ok(Map.of("message", "Código enviado"));
    }

    /** Paso 2: Verificar código (signup) */
    @PostMapping("/signup/verify")
    public ResponseEntity<?> verify(@Valid @RequestBody VerifyCodeRequest req) {
        signupService.verify(req);
        return ResponseEntity.ok(Map.of("verified", true));
    }

    /** Paso 3: Completar registro (signup) */
    @PostMapping("/signup/complete")
    public ResponseEntity<UsuarioDTO> complete(@Valid @RequestBody CompleteSignupRequest req) {
        UsuarioDTO created = signupService.complete(req);
        return ResponseEntity.ok(created);
    }

    /** Recuperar contraseña: envía la contraseña actual al email indicado (flujo solicitado) */
    @PostMapping("/password/forgot")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest req) {
        String email = normalizeEmail(req.getEmail());

        return usuarioRepository.findByEmail(email)
                .<ResponseEntity<?>>map((Usuario u) -> {
                    emailService.sendPasswordReminder(email, u.getContra());
                    return ResponseEntity.ok(Map.of("message", "Si el correo existe, se envió la contraseña."));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No existe un usuario con ese email.")));
    }

    /* ===================== CAMBIO DE EMAIL NUEVO ===================== */

    /** Paso 1: enviar código al nuevo email */
    @PostMapping("/email-change/start")
    public ResponseEntity<?> emailChangeStart(
            @Valid @RequestBody EmailChangeStartRequest req) {
        emailChangeService.start(req);
        return ResponseEntity.ok(Map.of("message", "Código enviado al nuevo correo."));
    }

    /** Paso 2: verificar código */
    @PostMapping("/email-change/verify")
    public ResponseEntity<?> emailChangeVerify(
            @Valid @RequestBody EmailChangeVerifyRequest req) {
        emailChangeService.verify(req);
        return ResponseEntity.ok(Map.of("verified", true));
    }

    /** Paso 3: aplicar el cambio */
    @PostMapping("/email-change/commit")
    public ResponseEntity<?> emailChangeCommit(
            @Valid @RequestBody EmailChangeCommitRequest req) {
        emailChangeService.commit(req);
        return ResponseEntity.ok(Map.of("message", "Correo actualizado correctamente."));
    }


}
