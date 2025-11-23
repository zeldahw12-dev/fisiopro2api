package com.fisio.fisio.repository;

import com.fisio.fisio.model.ZonaCuerpo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZonaCuerpoRepository extends JpaRepository<ZonaCuerpo, Integer> {
    Optional<ZonaCuerpo> findByNombre(String nombre);
    List<ZonaCuerpo> findByNombreContainingIgnoreCase(String nombre);
}
