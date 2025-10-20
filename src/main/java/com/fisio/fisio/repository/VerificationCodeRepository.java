package com.fisio.fisio.repository;

import com.fisio.fisio.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {

    // Último código NO usado para un email (sin filtrar expiración)
    Optional<VerificationCode> findTopByEmailAndUsedFalseOrderByIdDesc(String email);

    // Último código NO usado y NO expirado
    Optional<VerificationCode> findTopByEmailAndUsedFalseAndExpiresAtAfterOrderByIdDesc(
            String email, LocalDateTime now);

    // Último código USADO (para comprobar que sí se verificó antes de completar)
    Optional<VerificationCode> findTopByEmailAndUsedOrderByIdDesc(String email, boolean used);

    // Evita borrar: marcamos los anteriores como usados (invalidarlos)
    @Modifying
    @Query("update VerificationCode v set v.used = true where v.email = :email and v.used = false")
    int markAllUnusedAsUsedForEmail(@Param("email") String email);

    // Aún disponible si quisieras borrar todo el historial (no se usa en el flujo normal)
    void deleteByEmail(String email);
}
