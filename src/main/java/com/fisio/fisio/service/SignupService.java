package com.fisio.fisio.service;

import com.fisio.fisio.dto.UsuarioDTO;
import com.fisio.fisio.dto.signup.*;
import com.fisio.fisio.model.Usuario;
import com.fisio.fisio.model.VerificationCode;
import com.fisio.fisio.repository.UsuarioRepository;
import com.fisio.fisio.repository.VerificationCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
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

        String code = generateCode();
        VerificationCode v = new VerificationCode(
                email,
                code,
                LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(expirationMinutes)
        );
        v.setUsed(false);
        v.setAttempts(0);
        verificationRepo.save(v);

        CompletableFuture.runAsync(() -> invalidateOldAsync(email, v.getId()));

        emailService.sendVerificationCode(email, code);
    }

    private void invalidateOldAsync(String email, Integer keepId) {
        TransactionTemplate tx = new TransactionTemplate(txManager);
        tx.setTimeout(5);
        tx.executeWithoutResult(status -> {
            List<VerificationCode> olds =
                    verificationRepo.findAllUnusedByEmail(email, PageRequest.of(1, 10));
            for (VerificationCode old : olds) {
                if (!old.getId().equals(keepId)) {
                    verificationRepo.markUsedById(old.getId());
                }
            }
        });
    }

    @Transactional
    public void verify(VerifyCodeRequest req) {
        String email = normalizeEmail(req.getEmail());
        String code = req.getCode().trim();

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
                .orElseThrow(() -> new IllegalArgumentException("Verifica tu email antes de completar"));

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (!req.getContra().equals(req.getConfirmarContra())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        Usuario u = new Usuario();
        u.setNickname(req.getNickname().trim());
        u.setNombre(req.getNombre().trim());
        // ❌ ANTES: u.setFechaNacimiento(req.getFe());
        // ✅ AHORA: usamos el getter correcto del DTO de signup
        u.setFechaNacimiento(req.getFechaNacimiento());
        u.setEmail(email);
        u.setContra(req.getContra()); // hash pendiente
        u.setFoto(req.getFoto());
        u.setProfesion(req.getProfesion());

        Usuario saved = usuarioRepository.save(u);

        verificationRepo.findAllUnusedByEmail(email, PageRequest.of(0, 10))
                .forEach(vc -> verificationRepo.markUsedById(vc.getId()));

        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(saved.getIdUsuario());
        dto.setNickname(saved.getNickname());
        dto.setNombre(saved.getNombre());
        // ❌ ANTES: dto.setEdad(saved.getEdad());
        // ✅ AHORA:
        dto.setFechaNacimiento(saved.getFechaNacimiento());
        dto.setEmail(saved.getEmail());
        dto.setContra(saved.getContra());
        dto.setFoto(saved.getFoto());
        dto.setProfesion(saved.getProfesion());
        return dto;
    }
}
