package com.fisio.fisio.repository;

import com.fisio.fisio.model.NotaEvolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotaEvolucionRepository extends JpaRepository<NotaEvolucion, Integer> {

    List<NotaEvolucion> findByAgenda_IdAgenda(Integer agendaId);

    List<NotaEvolucion> findByZonaCuerpo_IdZonaCuerpo(Integer zonaCuerpoId);

    List<NotaEvolucion> findByAgenda_Paciente_IdPaciente(Integer pacienteId);

    // Nuevo método para validar unicidad por agenda y zona del cuerpo
    List<NotaEvolucion> findByAgenda_IdAgendaAndZonaCuerpo_IdZonaCuerpo(Integer agendaId, Integer zonaCuerpoId);
}
