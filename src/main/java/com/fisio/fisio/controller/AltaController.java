package com.fisio.fisio.controller;

import com.fisio.fisio.dto.AltaDTO;
import com.fisio.fisio.service.AltaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/altas")
@CrossOrigin(origins = "*")
public class AltaController {

    @Autowired
    private AltaService altaService;

    @GetMapping
    public ResponseEntity<List<AltaDTO>> getAllAltas() {
        List<AltaDTO> altas = altaService.findAll();
        return ResponseEntity.ok(altas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AltaDTO> getAltaById(@PathVariable Integer id) {
        return altaService.findById(id)
                .map(alta -> ResponseEntity.ok(alta))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AltaDTO> createAlta(@Valid @RequestBody AltaDTO altaDTO) {
        try {
            AltaDTO savedAlta = altaService.save(altaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAlta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AltaDTO> updateAlta(@PathVariable Integer id,
                                              @Valid @RequestBody AltaDTO altaDTO) {
        try {
            return altaService.update(id, altaDTO)
                    .map(alta -> ResponseEntity.ok(alta))
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlta(@PathVariable Integer id) {
        if (altaService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<AltaDTO>> getAltasByPaciente(@PathVariable Integer pacienteId) {
        List<AltaDTO> altas = altaService.findByPacienteId(pacienteId);
        return ResponseEntity.ok(altas);
    }

    @GetMapping("/buscar/nombre-paciente")
    public ResponseEntity<List<AltaDTO>> getAltasByNombrePaciente(@RequestParam String nombrePaciente) {
        List<AltaDTO> altas = altaService.findByNombrePaciente(nombrePaciente);
        return ResponseEntity.ok(altas);
    }

    @GetMapping("/buscar/expediente")
    public ResponseEntity<List<AltaDTO>> getAltasByNumeroExpediente(@RequestParam String numeroExpediente) {
        List<AltaDTO> altas = altaService.findByNumeroExpediente(numeroExpediente);
        return ResponseEntity.ok(altas);
    }

    @GetMapping("/buscar/fecha-alta")
    public ResponseEntity<List<AltaDTO>> getAltasByFechaAltaRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<AltaDTO> altas = altaService.findByFechaAltaRange(fechaInicio, fechaFin);
        return ResponseEntity.ok(altas);
    }

    @GetMapping("/buscar/terapeuta")
    public ResponseEntity<List<AltaDTO>> getAltasByNombreTerapeuta(@RequestParam String nombreTerapeuta) {
        List<AltaDTO> altas = altaService.findByNombreTerapeuta(nombreTerapeuta);
        return ResponseEntity.ok(altas);
    }
}
