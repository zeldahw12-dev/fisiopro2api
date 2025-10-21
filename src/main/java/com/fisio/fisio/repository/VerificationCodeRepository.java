package com.fisio.fisio.repository;

import com.fisio.fisio.model.VerificationCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {

    Optional<VerificationCode> findTopByEmailAndUsedFalseOrderByIdDesc(String email);

    Optional<VerificationCode> findTopByEmailAndUsedFalseAndExpiresAtAfterOrderByIdDesc(
            String email, LocalDateTime now);

    Optional<VerificationCode> findTopByEmailAndUsedOrderByIdDesc(String email, boolean used);

    /** Bloquea sólo el último código sin usar para ese email (fail-fast) */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "0") // no espera, evita 1205
    })
    @Query("""
       select v from VerificationCode v
       where v.email = :email and v.used = false
       order by v.createdAt desc
    """)
    List<VerificationCode> lockLatestUnusedByEmail(@Param("email") String email, Pageable pageable);

    /** Marca usado por id (1 fila) */
    @Modifying
    @Query("update VerificationCode v set v.used = true where v.id = :id and v.used = false")
    int markUsedById(@Param("id") Integer id);

    /** Invalida otros códigos pendientes, excepto el indicado */
    @Modifying
    @Query("""
       update VerificationCode v set v.used = true
       where v.email = :email and v.used = false and v.id <> :keepId
    """)
    int invalidateOthers(@Param("email") String email, @Param("keepId") Integer keepId);

    /** (Mantengo la antigua por compatibilidad, pero ya no se usa en start/verify) */
    @Modifying
    @Query("update VerificationCode v set v.used = true where v.email = :email and v.used = false")
    int markAllUnusedAsUsedForEmail(@Param("email") String email);

    void deleteByEmail(String email);
}
