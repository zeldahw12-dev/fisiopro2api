package com.fisio.fisio.controller;

import com.fisio.fisio.dto.UsuarioDTO;
import com.fisio.fisio.dto.signup.CompleteSignupRequest;
import com.fisio.fisio.dto.signup.StartSignupRequest;
import com.fisio.fisio.dto.signup.VerifyCodeRequest;
import com.fisio.fisio.dto.auth.ForgotPasswordRequest;
import com.fisio.fisio.dto.auth.ResetPasswordRequest;
import com.fisio.fisio.dto.auth.EmailChangeStartRequest;
import com.fisio.fisio.dto.auth.EmailChangeVerifyRequest;
import com.fisio.fisio.dto.auth.EmailChangeCommitRequest;
import com.fisio.fisio.model.Usuario;
import com.fisio.fisio.repository.UsuarioRepository;
import com.fisio.fisio.service.EmailChangeService;
import com.fisio.fisio.service.SignupService;
import com.fisio.fisio.service.PasswordResetService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final SignupService signupService;
    private final UsuarioRepository usuarioRepository;
    private final EmailChangeService emailChangeService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetService passwordResetService;

    @Value("${app.jwt.secret:dev-secret-please-change-and-set-env}")
    private String jwtSecret;

    @Value("${app.jwt.issuer:fisiopro}")
    private String jwtIssuer;

    @Value("${app.jwt.expiration-minutes:43200}") // 30 días por defecto
    private long jwtExpirationMinutes;

    public AuthController(SignupService signupService,
                          UsuarioRepository usuarioRepository,
                          EmailChangeService emailChangeService,
                          PasswordEncoder passwordEncoder,
                          PasswordResetService passwordResetService) {
        this.signupService = signupService;
        this.usuarioRepository = usuarioRepository;
        this.emailChangeService = emailChangeService;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String email = normalizeEmail(req.get("email"));
        String password = req.get("password");

        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        // ✅ Comparamos usando BCrypt
        if (usuario == null || !passwordEncoder.matches(password, usuario.getContra())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Credenciales inválidas"));
        }

        String token = generateToken(usuario);

        // Enviamos info del usuario sin exponer la contraseña
        Map<String, Object> userPayload = new LinkedHashMap<>();
        userPayload.put("idUsuario", usuario.getIdUsuario());
        userPayload.put("nickname", usuario.getNickname());
        userPayload.put("nombre", usuario.getNombre());
        userPayload.put("fechaNacimiento", usuario.getFechaNacimiento());
        userPayload.put("email", usuario.getEmail());
        userPayload.put("foto", usuario.getFoto());
        userPayload.put("profesion", usuario.getProfesion());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", userPayload
        ));
    }

    private String normalizeEmail(String raw) {
        return raw == null ? null : raw.trim().toLowerCase(Locale.ROOT);
    }

    /** Generar JWT consistente con tu SecurityConfig */
    private String generateToken(Usuario usuario) {
        if (jwtSecret == null || jwtSecret.length() < 32) {
            throw new IllegalStateException("app.jwt.secret es demasiado corto. Usa uno de al menos 32 caracteres.");
        }
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        Instant now = Instant.now();
        Instant exp = now.plus(jwtExpirationMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .setSubject(String.valueOf(usuario.getIdUsuario())) // subject = idUsuario
                .setIssuer(jwtIssuer != null && !jwtIssuer.isBlank() ? jwtIssuer : null)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("email", usuario.getEmail())
                .claim("nickname", usuario.getNickname())
                // Puedes cambiar el rol si después tienes admin, etc.
                .claim("roles", List.of("ROLE_USER"))
                .signWith(key)
                .compact();
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

    /* ========== NUEVO FLUJO: OLVIDÉ MI CONTRASEÑA (código + nueva contraseña) ========== */

    /** Paso 1: Usuario pone su email → se envía código al correo */
    @PostMapping("/password/forgot/start")
    public ResponseEntity<?> forgotPasswordStart(@Valid @RequestBody ForgotPasswordRequest req) {
        passwordResetService.startForgotPassword(req);
        return ResponseEntity.ok(
                Map.of("message", "Si el correo existe, se envió un código de recuperación.")
        );
    }

    /** Paso 2: Usuario ingresa el código → solo validamos que sea correcto */
    @PostMapping("/password/forgot/verify")
    public ResponseEntity<?> forgotPasswordVerify(@Valid @RequestBody VerifyCodeRequest req) {
        passwordResetService.verifyForgotPasswordCode(req);
        return ResponseEntity.ok(Map.of("verified", true));
    }

    /** Paso 3: Usuario escribe nueva contraseña + confirmación → se actualiza */
    @PostMapping("/password/forgot/reset")
    public ResponseEntity<?> forgotPasswordReset(@Valid @RequestBody ResetPasswordRequest req) {
        passwordResetService.resetPassword(req);
        return ResponseEntity.ok(
                Map.of("message", "Contraseña actualizada correctamente.")
        );
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
