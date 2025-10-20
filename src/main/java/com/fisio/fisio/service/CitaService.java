package com.fisio.fisio.service;

import com.fisio.fisio.dto.CitaDTO;
import com.fisio.fisio.model.Cita;
import com.fisio.fisio.model.Paciente;
import com.fisio.fisio.repository.CitaRepository;
import com.fisio.fisio.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<CitaDTO> findAll() {
        return citaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CitaDTO> findById(Integer id) {
        return citaRepository.findById(id)
                .map(this::convertToDTO);
    }

    public CitaDTO save(CitaDTO citaDTO) {
        Cita cita = convertToEntity(citaDTO);
        Cita savedCita = citaRepository.save(cita);
        return convertToDTO(savedCita);
    }

    public Optional<CitaDTO> update(Integer id, CitaDTO citaDTO) {
        return citaRepository.findById(id)
                .map(existingCita -> {
                    existingCita.setFecha(citaDTO.getFecha());

                    // Actualizar paciente si se proporciona
                    if (citaDTO.getIdPaciente() != null) {
                        Optional<Paciente> paciente = pacienteRepository.findById(citaDTO.getIdPaciente());
                        existingCita.setPaciente(paciente.orElse(null));
                    } else {
                        existingCita.setPaciente(null);
                    }

                    return convertToDTO(citaRepository.save(existingCita));
                });
    }

    public boolean deleteById(Integer id) {
        if (citaRepository.existsById(id)) {
            citaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<CitaDTO> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return citaRepository.findByFechaBetween(fechaInicio, fechaFin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Nuevo método para buscar citas por paciente
    public List<CitaDTO> findByPacienteId(Integer idPaciente) {
        return citaRepository.findByPacienteIdPaciente(idPaciente).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CitaDTO> findByUsuarioId(Integer idUsuario) {
        return citaRepository.findByUsuarioId(idUsuario).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CitaDTO> findByUsuarioIdAndFechaBetween(Integer idUsuario, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return citaRepository.findByUsuarioIdAndFechaBetween(idUsuario, fechaInicio, fechaFin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CitaDTO convertToDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setFecha(cita.getFecha());

        // Mapear información del paciente si existe
        if (cita.getPaciente() != null) {
            dto.setIdPaciente(cita.getPaciente().getIdPaciente());
            dto.setNombrePaciente(cita.getPaciente().getNombre());
        }

        return dto;
    }

    private Cita convertToEntity(CitaDTO citaDTO) {
        Cita cita = new Cita();
        cita.setIdCita(citaDTO.getIdCita());
        cita.setFecha(citaDTO.getFecha());

        // Asignar paciente si se proporciona el ID
        if (citaDTO.getIdPaciente() != null) {
            Optional<Paciente> paciente = pacienteRepository.findById(citaDTO.getIdPaciente());
            cita.setPaciente(paciente.orElse(null));
        }

        return cita;
    }
}
