package com.fisio.fisio.repository;

import com.fisio.fisio.model.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Integer> {

    List<HistoriaClinica> findByPaciente_IdPaciente(Integer idPaciente);

    List<HistoriaClinica> findByNombreContainingIgnoreCase(String nombre);

    List<HistoriaClinica> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<HistoriaClinica> findByFisioterapeutaContainingIgnoreCase(String fisioterapeuta);

    List<HistoriaClinica> findByMedicoTratanteContainingIgnoreCase(String medicoTratante);

    @Query("SELECT h FROM HistoriaClinica h WHERE h.paciente.idPaciente = :idPaciente ORDER BY h.fecha DESC")
    List<HistoriaClinica> findByPacienteOrderByFechaDesc(@Param("idPaciente") Integer idPaciente);

    @Query("SELECT h FROM HistoriaClinica h WHERE h.diagnosticoMedico LIKE %:diagnostico%")
    List<HistoriaClinica> findByDiagnosticoMedicoContaining(@Param("diagnostico") String diagnostico);

    @Query("SELECT h FROM HistoriaClinica h WHERE h.diagnosticoFisioterapeutico LIKE %:diagnostico%")
    List<HistoriaClinica> findByDiagnosticoFisioterapeuticoContaining(@Param("diagnostico") String diagnostico);

    @Query("SELECT COUNT(h) FROM HistoriaClinica h WHERE h.paciente.idPaciente = :idPaciente")
    Long countByPaciente(@Param("idPaciente") Integer idPaciente);

    // Nuevas consultas para los campos agregados
    List<HistoriaClinica> findByEscolaridadContainingIgnoreCase(String escolaridad);

    @Query("SELECT h FROM HistoriaClinica h WHERE h.padecimientoActual LIKE %:padecimiento%")
    List<HistoriaClinica> findByPadecimientoActualContaining(@Param("padecimiento") String padecimiento);

    @Query("SELECT h FROM HistoriaClinica h WHERE h.pruebasEspeciales LIKE %:pruebas%")
    List<HistoriaClinica> findByPruebasEspecialesContaining(@Param("pruebas") String pruebas);
}
