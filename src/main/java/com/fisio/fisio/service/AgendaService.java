package com.fisio.fisio.service;

import com.fisio.fisio.dto.AgendaDTO;
import com.fisio.fisio.dto.PacienteInfoCompletaDTO;
import com.fisio.fisio.model.Agenda;
import com.fisio.fisio.model.Cita;
import com.fisio.fisio.model.HistoriaClinica;
import com.fisio.fisio.model.Paciente;
import com.fisio.fisio.repository.AgendaRepository;
import com.fisio.fisio.repository.CitaRepository;
import com.fisio.fisio.repository.HistoriaClinicaRepository;
import com.fisio.fisio.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;


    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CitaRepository citaRepository;

    public List<AgendaDTO> findAll() {
        return agendaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AgendaDTO> findById(Integer id) {
        return agendaRepository.findById(id)
                .map(this::convertToDTO);
    }
    public Optional<PacienteInfoCompletaDTO> obtenerInfoCompletaPaciente(Integer pacienteId) {
        // Verificar que el paciente existe
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(pacienteId);
        if (pacienteOpt.isEmpty()) {
            return Optional.empty();
        }

        Paciente paciente = pacienteOpt.get();

        // Obtener la historia clínica más reciente del paciente
        List<HistoriaClinica> historias = historiaClinicaRepository.findByPaciente_IdPaciente(pacienteId);
        HistoriaClinica historiaReciente = historias.stream()
                .max((h1, h2) -> h1.getFechaCreacion().compareTo(h2.getFechaCreacion()))
                .orElse(null);

        // Obtener todas las agendas del paciente
        List<Agenda> agendas = agendaRepository.findByPaciente_IdPaciente(pacienteId);

        // Convertir agendas a DTO
        List<PacienteInfoCompletaDTO.AgendaInfoDTO> agendasInfo = agendas.stream()
                .map(agenda -> new PacienteInfoCompletaDTO.AgendaInfoDTO(
                        agenda.getFecha(),
                        agenda.getIntervencion()
                ))
                .collect(Collectors.toList());

        // Crear el DTO de respuesta
        PacienteInfoCompletaDTO infoCompleta = new PacienteInfoCompletaDTO();
        infoCompleta.setNombrePaciente(paciente.getNombre());

        // Información de la historia clínica (si existe)
        if (historiaReciente != null) {
            infoCompleta.setSeguro(historiaReciente.getAseguradora());
            infoCompleta.setDiagnosticoMedico(historiaReciente.getDiagnosticoMedico());
            infoCompleta.setCedulaFisioterapeuta(historiaReciente.getCedula());
            infoCompleta.setNombreFisioterapeuta(historiaReciente.getFisioterapeuta());
        } else {
            // Si no hay historia clínica, usar información básica del paciente
            infoCompleta.setDiagnosticoMedico(paciente.getDiagnosticoMedico());
            infoCompleta.setSeguro(null);
            infoCompleta.setCedulaFisioterapeuta(null);
            infoCompleta.setNombreFisioterapeuta(null);
        }

        infoCompleta.setAgendas(agendasInfo);

        return Optional.of(infoCompleta);
    }
    public AgendaDTO save(AgendaDTO agendaDTO) {
        // Validar que la cita existe
        Cita cita = citaRepository.findById(agendaDTO.getCitaId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + agendaDTO.getCitaId()));

        // Determinar qué paciente usar para la agenda
        Integer pacienteIdFinal;
        if (cita.getPaciente() != null) {
            // Si la cita tiene un paciente asignado, usar ese paciente para la agenda
            pacienteIdFinal = cita.getPaciente().getIdPaciente();
            System.out.println("Usando paciente de la cita: " + pacienteIdFinal);
        } else {
            // Si la cita no tiene paciente, usar el paciente del DTO
            pacienteIdFinal = agendaDTO.getPacienteId();
            System.out.println("Usando paciente del DTO: " + pacienteIdFinal);
        }

        // Validar que el paciente existe
        Paciente paciente = pacienteRepository.findById(pacienteIdFinal)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + pacienteIdFinal));

        // Verificar que no existe ya una agenda para esta cita
        List<Agenda> existingAgendas = agendaRepository.findByCita_IdCita(agendaDTO.getCitaId());
        if (!existingAgendas.isEmpty()) {
            throw new RuntimeException("Ya existe una agenda para esta cita");
        }

        Agenda agenda = convertToEntity(agendaDTO);
        agenda.setPaciente(paciente);
        agenda.setCita(cita);

        Agenda savedAgenda = agendaRepository.save(agenda);
        return convertToDTO(savedAgenda);
    }

    public Optional<AgendaDTO> update(Integer id, AgendaDTO agendaDTO) {
        return agendaRepository.findById(id)
                .map(existingAgenda -> {
                    existingAgenda.setFecha(agendaDTO.getFecha());
                    existingAgenda.setIntervencion(agendaDTO.getIntervencion());
                    existingAgenda.setObservaciones(agendaDTO.getObservaciones());

                    // Actualizar paciente si es necesario
                    if (!existingAgenda.getPaciente().getIdPaciente().equals(agendaDTO.getPacienteId())) {
                        Paciente paciente = pacienteRepository.findById(agendaDTO.getPacienteId())
                                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
                        existingAgenda.setPaciente(paciente);
                    }

                    // Actualizar cita si es necesario
                    if (!existingAgenda.getCita().getIdCita().equals(agendaDTO.getCitaId())) {
                        Cita cita = citaRepository.findById(agendaDTO.getCitaId())
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
                        existingAgenda.setCita(cita);
                    }

                    return convertToDTO(agendaRepository.save(existingAgenda));
                });
    }

    public boolean deleteById(Integer id) {
        if (agendaRepository.existsById(id)) {
            agendaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<AgendaDTO> findByPacienteId(Integer pacienteId) {
        return agendaRepository.findByPaciente_IdPaciente(pacienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AgendaDTO> findByCitaId(Integer citaId) {
        return agendaRepository.findByCita_IdCita(citaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AgendaDTO> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return agendaRepository.findByFechaBetween(fechaInicio, fechaFin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AgendaDTO convertToDTO(Agenda agenda) {
        AgendaDTO dto = new AgendaDTO();
        dto.setIdAgenda(agenda.getIdAgenda());
        dto.setFecha(agenda.getFecha());
        dto.setIntervencion(agenda.getIntervencion());
        dto.setObservaciones(agenda.getObservaciones());
        dto.setPacienteId(agenda.getPaciente().getIdPaciente());
        dto.setCitaId(agenda.getCita().getIdCita());

        // Información adicional para el frontend
        dto.setNombrePaciente(agenda.getPaciente().getNombre());

        return dto;
    }

    private Agenda convertToEntity(AgendaDTO agendaDTO) {
        Agenda agenda = new Agenda();
        agenda.setIdAgenda(agendaDTO.getIdAgenda());
        agenda.setFecha(agendaDTO.getFecha());
        agenda.setIntervencion(agendaDTO.getIntervencion());
        agenda.setObservaciones(agendaDTO.getObservaciones());

        // Las relaciones se setean en el método save/update
        return agenda;
    }
}
