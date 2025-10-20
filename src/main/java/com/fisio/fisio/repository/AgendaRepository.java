package com.fisio.fisio.repository;

import com.fisio.fisio.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Integer> {
    List<Agenda> findByPaciente_IdPaciente(Integer pacienteId);
    List<Agenda> findByCita_IdCita(Integer citaId);
    List<Agenda> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
