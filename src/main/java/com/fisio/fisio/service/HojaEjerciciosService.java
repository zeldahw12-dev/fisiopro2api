package com.fisio.fisio.service;

import com.fisio.fisio.dto.HojaEjerciciosDTO;
import com.fisio.fisio.model.HojaEjercicios;
import com.fisio.fisio.model.Paciente;
import com.fisio.fisio.repository.HojaEjerciciosRepository;
import com.fisio.fisio.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HojaEjerciciosService {

    @Autowired
    private HojaEjerciciosRepository hojaEjerciciosRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<HojaEjerciciosDTO> findAll() {
        return hojaEjerciciosRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<HojaEjerciciosDTO> findById(Integer id) {
        return hojaEjerciciosRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<HojaEjerciciosDTO> findByPacienteId(Integer pacienteId) {
        return hojaEjerciciosRepository.findByPaciente_IdPaciente(pacienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HojaEjerciciosDTO> findByTituloLike(String titulo) {
        return hojaEjerciciosRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public HojaEjerciciosDTO save(HojaEjerciciosDTO dto) {
        HojaEjercicios entity = convertToEntity(dto);
        LocalDateTime now = LocalDateTime.now();
        entity.setFechaCreacion(now);
        entity.setFechaActualizacion(now);
        HojaEjercicios saved = hojaEjerciciosRepository.save(entity);
        return convertToDTO(saved);
    }

    public Optional<HojaEjerciciosDTO> update(Integer id, HojaEjerciciosDTO dto) {
        return hojaEjerciciosRepository.findById(id)
                .map(existing -> {
                    // Solo campos editables
                    existing.setTitulo(dto.getTitulo());
                    existing.setEjerciciosTexto(dto.getEjerciciosTexto());
                    existing.setObjetivos(dto.getObjetivos());
                    existing.setRecomendaciones(dto.getRecomendaciones());
                    existing.setVideoUrls(joinUrls(dto.getVideoUrls()));
                    // Se puede permitir mover de paciente si lo deseas:
                    if (dto.getPacienteId() != null && !dto.getPacienteId().equals(existing.getPaciente().getIdPaciente())) {
                        Paciente p = pacienteRepository.findById(dto.getPacienteId())
                                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
                        existing.setPaciente(p);
                    }
                    existing.setFechaActualizacion(LocalDateTime.now());
                    return convertToDTO(hojaEjerciciosRepository.save(existing));
                });
    }

    /** Borrado simple; si quieres borrado profundo, ajusta cascadas en relaciones hijas (aquí no hay hijas). */
    @Transactional
    public boolean deleteById(Integer id) {
        return hojaEjerciciosRepository.findById(id).map(e -> {
            hojaEjerciciosRepository.delete(e);
            return true;
        }).orElse(false);
    }

    // ===== Mapas DTO <-> Entity =====

    private HojaEjerciciosDTO convertToDTO(HojaEjercicios e) {
        HojaEjerciciosDTO dto = new HojaEjerciciosDTO();
        dto.setIdHoja(e.getIdHoja());
        dto.setPacienteId(e.getPaciente().getIdPaciente());
        dto.setTitulo(e.getTitulo());
        dto.setEjerciciosTexto(e.getEjerciciosTexto());
        dto.setObjetivos(e.getObjetivos());
        dto.setRecomendaciones(e.getRecomendaciones());
        dto.setVideoUrls(splitUrls(e.getVideoUrls()));
        return dto;
    }

    private HojaEjercicios convertToEntity(HojaEjerciciosDTO dto) {
        HojaEjercicios e = new HojaEjercicios();
        e.setIdHoja(dto.getIdHoja());

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        e.setPaciente(paciente);

        e.setTitulo(dto.getTitulo());
        e.setEjerciciosTexto(dto.getEjerciciosTexto());
        e.setObjetivos(dto.getObjetivos());
        e.setRecomendaciones(dto.getRecomendaciones());
        e.setVideoUrls(joinUrls(dto.getVideoUrls()));
        // fechas se setean en save/update
        return e;
    }

    private List<String> splitUrls(String raw) {
        if (raw == null || raw.isBlank()) return Collections.emptyList();
        return Arrays.stream(raw.split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
    }

    private String joinUrls(List<String> urls) {
        if (urls == null || urls.isEmpty()) return null;
        // Normaliza espacios
        List<String> cleaned = urls.stream()
                .map(s -> s == null ? "" : s.trim())
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
        if (cleaned.isEmpty()) return null;
        return String.join("\n", cleaned);
    }
}
