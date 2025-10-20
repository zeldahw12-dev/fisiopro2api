package com.fisio.fisio.repository;

import com.fisio.fisio.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Método para buscar citas por paciente
    List<Cita> findByPacienteIdPaciente(Integer idPaciente);

    // Método para buscar citas sin paciente asignado
    List<Cita> findByPacienteIsNull();

    @Query("SELECT c FROM Cita c WHERE c.paciente.usuario.idUsuario = :idUsuario")
    List<Cita> findByUsuarioId(@Param("idUsuario") Integer idUsuario);

    @Query("SELECT c FROM Cita c WHERE c.paciente.usuario.idUsuario = :idUsuario AND c.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Cita> findByUsuarioIdAndFechaBetween(@Param("idUsuario") Integer idUsuario,
                                              @Param("fechaInicio") LocalDateTime fechaInicio,
                                              @Param("fechaFin") LocalDateTime fechaFin);
}
