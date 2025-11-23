package com.fisio.fisio.service;

import com.fisio.fisio.dto.AltaDTO;
import com.fisio.fisio.model.Alta;
import com.fisio.fisio.model.Paciente;
import com.fisio.fisio.repository.AltaRepository;
import com.fisio.fisio.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional // Adding transactional annotation for data synchronization
public class AltaService {

    @Autowired
    private AltaRepository altaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired

    public List<AltaDTO> findAll() {
        return altaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AltaDTO> findById(Integer id) {
        return altaRepository.findById(id)
                .map(this::convertToDTO);
    }

    public AltaDTO save(AltaDTO altaDTO) {
        Alta alta = convertToEntity(altaDTO);

        if (alta.getPaciente() != null) {
            Paciente paciente = alta.getPaciente();
            if (alta.getNombrePaciente() == null || alta.getNombrePaciente().isEmpty()) {
                alta.setNombrePaciente(paciente.getNombre());
            }
            if (alta.getEdad() == null) {
                alta.setEdad(paciente.getEdad());
            }
            if (alta.getDiagnosticoFinal() == null || alta.getDiagnosticoFinal().isEmpty()) {
                alta.setDiagnosticoFinal(paciente.getDiagnosticoMedico());
            }
        }

        Alta savedAlta = altaRepository.save(alta);
        return convertToDTO(savedAlta);
    }

    public Optional<AltaDTO> update(Integer id, AltaDTO altaDTO) {
        return altaRepository.findById(id)
                .map(existingAlta -> {
                    String originalNombre = existingAlta.getNombrePaciente();
                    Integer originalEdad = existingAlta.getEdad();
                    String originalDiagnostico = existingAlta.getDiagnosticoFinal();

                    existingAlta.setNumeroExpediente(altaDTO.getNumeroExpediente());
                    existingAlta.setNombrePaciente(altaDTO.getNombrePaciente());
                    existingAlta.setEdad(altaDTO.getEdad());
                    existingAlta.setFechaNacimiento(altaDTO.getFechaNacimiento());
                    existingAlta.setSexo(altaDTO.getSexo());
                    existingAlta.setDiagnosticoFinal(altaDTO.getDiagnosticoFinal());
                    existingAlta.setFechaIngreso(altaDTO.getFechaIngreso());
                    existingAlta.setFechaAlta(altaDTO.getFechaAlta());
                    existingAlta.setSecuelas(altaDTO.getSecuelas());
                    existingAlta.setMotivoAlta(altaDTO.getMotivoAlta());
                    existingAlta.setServiciosOtorgados(altaDTO.getServiciosOtorgados());
                    existingAlta.setNombreTerapeuta(altaDTO.getNombreTerapeuta());
                    existingAlta.setObservaciones(altaDTO.getObservaciones());

                    Paciente paciente = pacienteRepository.findById(altaDTO.getPacienteId())
                            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
                    existingAlta.setPaciente(paciente);

                    Alta savedAlta = altaRepository.save(existingAlta);

                    boolean needsSync = !originalNombre.equals(altaDTO.getNombrePaciente()) ||
                            !originalEdad.equals(altaDTO.getEdad()) ||
                            (originalDiagnostico != null && !originalDiagnostico.equals(altaDTO.getDiagnosticoFinal()));



                    return convertToDTO(savedAlta);
                });
    }

    public boolean deleteById(Integer id) {
        if (altaRepository.existsById(id)) {
            altaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<AltaDTO> findByPacienteId(Integer pacienteId) {
        return altaRepository.findByPaciente_IdPaciente(pacienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AltaDTO> findByNombrePaciente(String nombrePaciente) {
        return altaRepository.findByNombrePacienteContainingIgnoreCase(nombrePaciente).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AltaDTO> findByNumeroExpediente(String numeroExpediente) {
        return altaRepository.findByNumeroExpediente(numeroExpediente).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AltaDTO> findByFechaAltaRange(LocalDate fechaInicio, LocalDate fechaFin) {
        return altaRepository.findByFechaAltaBetween(fechaInicio, fechaFin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AltaDTO> findByNombreTerapeuta(String nombreTerapeuta) {
        return altaRepository.findByNombreTerapeutaContainingIgnoreCase(nombreTerapeuta).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AltaDTO convertToDTO(Alta alta) {
        AltaDTO dto = new AltaDTO();
        dto.setIdAlta(alta.getIdAlta());
        dto.setNumeroExpediente(alta.getNumeroExpediente());
        dto.setNombrePaciente(alta.getNombrePaciente());
        dto.setEdad(alta.getEdad());
        dto.setFechaNacimiento(alta.getFechaNacimiento());
        dto.setSexo(alta.getSexo());
        dto.setDiagnosticoFinal(alta.getDiagnosticoFinal());
        dto.setFechaIngreso(alta.getFechaIngreso());
        dto.setFechaAlta(alta.getFechaAlta());
        dto.setSecuelas(alta.getSecuelas());
        dto.setMotivoAlta(alta.getMotivoAlta());
        dto.setServiciosOtorgados(alta.getServiciosOtorgados());
        dto.setNombreTerapeuta(alta.getNombreTerapeuta());
        dto.setObservaciones(alta.getObservaciones());
        dto.setPacienteId(alta.getPaciente().getIdPaciente());
        return dto;
    }

    private Alta convertToEntity(AltaDTO altaDTO) {
        Alta alta = new Alta();
        alta.setIdAlta(altaDTO.getIdAlta());
        alta.setNumeroExpediente(altaDTO.getNumeroExpediente());
        alta.setNombrePaciente(altaDTO.getNombrePaciente());
        alta.setEdad(altaDTO.getEdad());
        alta.setFechaNacimiento(altaDTO.getFechaNacimiento());
        alta.setSexo(altaDTO.getSexo());
        alta.setDiagnosticoFinal(altaDTO.getDiagnosticoFinal());
        alta.setFechaIngreso(altaDTO.getFechaIngreso());
        alta.setFechaAlta(altaDTO.getFechaAlta());
        alta.setSecuelas(altaDTO.getSecuelas());
        alta.setMotivoAlta(altaDTO.getMotivoAlta());
        alta.setServiciosOtorgados(altaDTO.getServiciosOtorgados());
        alta.setNombreTerapeuta(altaDTO.getNombreTerapeuta());
        alta.setObservaciones(altaDTO.getObservaciones());

        Paciente paciente = pacienteRepository.findById(altaDTO.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        alta.setPaciente(paciente);

        return alta;
    }
}
