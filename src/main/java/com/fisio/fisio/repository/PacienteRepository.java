package com.fisio.fisio.repository;

import com.fisio.fisio.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    List<Paciente> findByUsuario_IdUsuario(Integer usuarioId);
    List<Paciente> findByNombreContainingIgnoreCase(String nombre);
}
