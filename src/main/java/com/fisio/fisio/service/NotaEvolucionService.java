package com.fisio.fisio.service;

import com.fisio.fisio.dto.NotaEvolucionDTO;
import com.fisio.fisio.model.Agenda;
import com.fisio.fisio.model.NotaEvolucion;
import com.fisio.fisio.model.ZonaCuerpo;
import com.fisio.fisio.repository.AgendaRepository;
import com.fisio.fisio.repository.NotaEvolucionRepository;
import com.fisio.fisio.repository.ZonaCuerpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotaEvolucionService {

    @Autowired
    private NotaEvolucionRepository notaEvolucionRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private ZonaCuerpoRepository zonaCuerpoRepository;

    public List<NotaEvolucionDTO> findAll() {
        return notaEvolucionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<NotaEvolucionDTO> findById(Integer id) {
        return notaEvolucionRepository.findById(id)
                .map(this::convertToDTO);
    }

    public NotaEvolucionDTO save(NotaEvolucionDTO notaEvolucionDTO) {
        List<NotaEvolucion> existingNotas = notaEvolucionRepository
                .findByAgenda_IdAgendaAndZonaCuerpo_IdZonaCuerpo(
                        notaEvolucionDTO.getAgendaId(),
                        notaEvolucionDTO.getZonaCuerpoId()
                );

        if (!existingNotas.isEmpty()) {
            throw new RuntimeException("Ya existe una nota de evolución para esta agenda y zona del cuerpo");
        }

        NotaEvolucion notaEvolucion = convertToEntity(notaEvolucionDTO);
        NotaEvolucion savedNotaEvolucion = notaEvolucionRepository.save(notaEvolucion);
        return convertToDTO(savedNotaEvolucion);
    }

    public Optional<NotaEvolucionDTO> update(Integer id, NotaEvolucionDTO notaEvolucionDTO) {
        return notaEvolucionRepository.findById(id)
                .map(existingNota -> {
                    existingNota.setFlexion(notaEvolucionDTO.getFlexion());
                    existingNota.setExtension(notaEvolucionDTO.getExtension());
                    existingNota.setAbduccion(notaEvolucionDTO.getAbduccion());
                    existingNota.setAduccion(notaEvolucionDTO.getAduccion());
                    existingNota.setRotacionInterna(notaEvolucionDTO.getRotacionInterna());
                    existingNota.setRotacionExterna(notaEvolucionDTO.getRotacionExterna());
                    existingNota.setPronacion(notaEvolucionDTO.getPronacion());
                    existingNota.setSupinacion(notaEvolucionDTO.getSupinacion());
                    existingNota.setFlexionPalmar(notaEvolucionDTO.getFlexionPalmar());
                    existingNota.setExtensionDorsal(notaEvolucionDTO.getExtensionDorsal());
                    existingNota.setDesviacionRadial(notaEvolucionDTO.getDesviacionRadial());
                    existingNota.setDesviacionCubital(notaEvolucionDTO.getDesviacionCubital());
                    existingNota.setOposicion(notaEvolucionDTO.getOposicion());
                    existingNota.setFlexionPlantar(notaEvolucionDTO.getFlexionPlantar());
                    existingNota.setInversion(notaEvolucionDTO.getInversion());
                    existingNota.setEversion(notaEvolucionDTO.getEversion());
                    existingNota.setAbduccionAduccion(notaEvolucionDTO.getAbduccionAduccion());
                    existingNota.setInclinacionLateral(notaEvolucionDTO.getInclinacionLateral());
                    existingNota.setRotacion(notaEvolucionDTO.getRotacion());

                    if (!existingNota.getAgenda().getIdAgenda().equals(notaEvolucionDTO.getAgendaId())) {
                        Agenda agenda = agendaRepository.findById(notaEvolucionDTO.getAgendaId())
                                .orElseThrow(() -> new RuntimeException("Agenda no encontrada"));
                        existingNota.setAgenda(agenda);
                    }

                    if (!existingNota.getZonaCuerpo().getIdZonaCuerpo().equals(notaEvolucionDTO.getZonaCuerpoId())) {
                        ZonaCuerpo zonaCuerpo = zonaCuerpoRepository.findById(notaEvolucionDTO.getZonaCuerpoId())
                                .orElseThrow(() -> new RuntimeException("Zona del cuerpo no encontrada"));
                        existingNota.setZonaCuerpo(zonaCuerpo);
                    }

                    return convertToDTO(notaEvolucionRepository.save(existingNota));
                });
    }

    public boolean deleteById(Integer id) {
        if (notaEvolucionRepository.existsById(id)) {
            notaEvolucionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<NotaEvolucionDTO> findByAgendaId(Integer agendaId) {
        return notaEvolucionRepository.findByAgenda_IdAgenda(agendaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotaEvolucionDTO> findByZonaCuerpoId(Integer zonaCuerpoId) {
        return notaEvolucionRepository.findByZonaCuerpo_IdZonaCuerpo(zonaCuerpoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotaEvolucionDTO> findByPacienteId(Integer pacienteId) {
        return notaEvolucionRepository.findByAgenda_Paciente_IdPaciente(pacienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private NotaEvolucionDTO convertToDTO(NotaEvolucion notaEvolucion) {
        NotaEvolucionDTO dto = new NotaEvolucionDTO();
        dto.setIdNotaEvolucion(notaEvolucion.getIdNotaEvolucion());

        dto.setFlexion(notaEvolucion.getFlexion());
        dto.setExtension(notaEvolucion.getExtension());
        dto.setAbduccion(notaEvolucion.getAbduccion());
        dto.setAduccion(notaEvolucion.getAduccion());
        dto.setRotacionInterna(notaEvolucion.getRotacionInterna());
        dto.setRotacionExterna(notaEvolucion.getRotacionExterna());
        dto.setPronacion(notaEvolucion.getPronacion());
        dto.setSupinacion(notaEvolucion.getSupinacion());
        dto.setFlexionPalmar(notaEvolucion.getFlexionPalmar());
        dto.setExtensionDorsal(notaEvolucion.getExtensionDorsal());
        dto.setDesviacionRadial(notaEvolucion.getDesviacionRadial());
        dto.setDesviacionCubital(notaEvolucion.getDesviacionCubital());
        dto.setOposicion(notaEvolucion.getOposicion());
        dto.setFlexionPlantar(notaEvolucion.getFlexionPlantar());
        dto.setInversion(notaEvolucion.getInversion());
        dto.setEversion(notaEvolucion.getEversion());
        dto.setAbduccionAduccion(notaEvolucion.getAbduccionAduccion());
        dto.setInclinacionLateral(notaEvolucion.getInclinacionLateral());
        dto.setRotacion(notaEvolucion.getRotacion());

        dto.setAgendaId(notaEvolucion.getAgenda().getIdAgenda());
        dto.setZonaCuerpoId(notaEvolucion.getZonaCuerpo().getIdZonaCuerpo());
        dto.setNombreZonaCuerpo(notaEvolucion.getZonaCuerpo().getNombre());

        return dto;
    }

    private NotaEvolucion convertToEntity(NotaEvolucionDTO notaEvolucionDTO) {
        NotaEvolucion notaEvolucion = new NotaEvolucion();
        notaEvolucion.setIdNotaEvolucion(notaEvolucionDTO.getIdNotaEvolucion());

        notaEvolucion.setFlexion(notaEvolucionDTO.getFlexion());
        notaEvolucion.setExtension(notaEvolucionDTO.getExtension());
        notaEvolucion.setAbduccion(notaEvolucionDTO.getAbduccion());
        notaEvolucion.setAduccion(notaEvolucionDTO.getAduccion());
        notaEvolucion.setRotacionInterna(notaEvolucionDTO.getRotacionInterna());
        notaEvolucion.setRotacionExterna(notaEvolucionDTO.getRotacionExterna());
        notaEvolucion.setPronacion(notaEvolucionDTO.getPronacion());
        notaEvolucion.setSupinacion(notaEvolucionDTO.getSupinacion());
        notaEvolucion.setFlexionPalmar(notaEvolucionDTO.getFlexionPalmar());
        notaEvolucion.setExtensionDorsal(notaEvolucionDTO.getExtensionDorsal());
        notaEvolucion.setDesviacionRadial(notaEvolucionDTO.getDesviacionRadial());
        notaEvolucion.setDesviacionCubital(notaEvolucionDTO.getDesviacionCubital());
        notaEvolucion.setOposicion(notaEvolucionDTO.getOposicion());
        notaEvolucion.setFlexionPlantar(notaEvolucionDTO.getFlexionPlantar());
        notaEvolucion.setInversion(notaEvolucionDTO.getInversion());
        notaEvolucion.setEversion(notaEvolucionDTO.getEversion());
        notaEvolucion.setAbduccionAduccion(notaEvolucionDTO.getAbduccionAduccion());
        notaEvolucion.setInclinacionLateral(notaEvolucionDTO.getInclinacionLateral());
        notaEvolucion.setRotacion(notaEvolucionDTO.getRotacion());

        Agenda agenda = agendaRepository.findById(notaEvolucionDTO.getAgendaId())
                .orElseThrow(() -> new RuntimeException("Agenda no encontrada"));
        notaEvolucion.setAgenda(agenda);

        ZonaCuerpo zonaCuerpo = zonaCuerpoRepository.findById(notaEvolucionDTO.getZonaCuerpoId())
                .orElseThrow(() -> new RuntimeException("Zona del cuerpo no encontrada"));
        notaEvolucion.setZonaCuerpo(zonaCuerpo);

        return notaEvolucion;
    }
}
