package com.fisio.fisio.repository;

import com.fisio.fisio.model.VerificationCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {

    // último código sin usar ni expirar
    Optional<VerificationCode> findTopByEmailAndUsedFalseAndExpiresAtAfterOrderByIdDesc(
            String email, LocalDateTime now);

    // último código ya usado (para el paso "complete")
    Optional<VerificationCode> findTopByEmailAndUsedOrderByIdDesc(String email, boolean used);

    // --- NUEVOS MÉTODOS OPTIMIZADOS ---
    @Query("""
        select v from VerificationCode v
        where v.email = :email and v.used = false
        order by v.id desc
    """)
    List<VerificationCode> findAllUnusedByEmail(@Param("email") String email, Pageable pageable);

    @Modifying
    @Query("update VerificationCode v set v.used = true where v.id = :id and v.used = false")
    int markUsedById(@Param("id") Integer id);
}
