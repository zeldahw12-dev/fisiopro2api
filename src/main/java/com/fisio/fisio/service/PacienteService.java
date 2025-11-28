package com.fisio.fisio.service;

import com.fisio.fisio.dto.PacienteDTO;
import com.fisio.fisio.model.Paciente;
import com.fisio.fisio.model.Usuario;
import com.fisio.fisio.repository.PacienteRepository;
import com.fisio.fisio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<PacienteDTO> findAll() {
        return pacienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PacienteDTO> findById(Integer id) {
        return pacienteRepository.findById(id)
                .map(this::convertToDTO);
    }

    public PacienteDTO save(PacienteDTO pacienteDTO) {
        Paciente paciente = convertToEntity(pacienteDTO);
        Paciente savedPaciente = pacienteRepository.save(paciente);
        return convertToDTO(savedPaciente);
    }

    public Optional<PacienteDTO> update(Integer id, PacienteDTO pacienteDTO) {
        return pacienteRepository.findById(id)
                .map(existingPaciente -> {
                    existingPaciente.setNombre(pacienteDTO.getNombre());
                    // <CHANGE> Replaced setEdad with setFechaNacimiento
                    existingPaciente.setFechaNacimiento(pacienteDTO.getFechaNacimiento());
                    existingPaciente.setDiagnosticoMedico(pacienteDTO.getDiagnosticoMedico());

                    Usuario usuario = usuarioRepository.findById(pacienteDTO.getUsuarioId())
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    existingPaciente.setUsuario(usuario);

                    return convertToDTO(pacienteRepository.save(existingPaciente));
                });
    }

    /** Borrado profundo con cascada JPA (y ON DELETE CASCADE si existe en BD) */
    @Transactional
    public boolean deleteById(Integer id) {
        return pacienteRepository.findById(id).map(p -> {
            pacienteRepository.delete(p);
            return true;
        }).orElse(false);
    }

    public List<PacienteDTO> findByUsuarioId(Integer usuarioId) {
        return pacienteRepository.findByUsuario_IdUsuario(usuarioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PacienteDTO convertToDTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setIdPaciente(paciente.getIdPaciente());
        dto.setNombre(paciente.getNombre());
        // <CHANGE> Replaced setEdad with setFechaNacimiento
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setDiagnosticoMedico(paciente.getDiagnosticoMedico());
        dto.setUsuarioId(paciente.getUsuario().getIdUsuario());
        return dto;
    }

    private Paciente convertToEntity(PacienteDTO pacienteDTO) {
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(pacienteDTO.getIdPaciente());
        paciente.setNombre(pacienteDTO.getNombre());
        // <CHANGE> Replaced setEdad with setFechaNacimiento
        paciente.setFechaNacimiento(pacienteDTO.getFechaNacimiento());
        paciente.setDiagnosticoMedico(pacienteDTO.getDiagnosticoMedico());

        Usuario usuario = usuarioRepository.findById(pacienteDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        paciente.setUsuario(usuario);

        return paciente;
    }
}