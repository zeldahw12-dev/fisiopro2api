package com.fisio.fisio.repository;

import com.fisio.fisio.model.Alta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AltaRepository extends JpaRepository<Alta, Integer> {
    List<Alta> findByPaciente_IdPaciente(Integer pacienteId);
    List<Alta> findByNombrePacienteContainingIgnoreCase(String nombrePaciente);
    List<Alta> findByNumeroExpediente(String numeroExpediente);
    List<Alta> findByFechaAltaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<Alta> findByNombreTerapeutaContainingIgnoreCase(String nombreTerapeuta);
}
