package com.fisio.fisio.controller;

import com.fisio.fisio.dto.PacienteDTO;
import com.fisio.fisio.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> getAllPacientes() {
        List<PacienteDTO> pacientes = pacienteService.findAll();
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> getPacienteById(@PathVariable Integer id) {
        return pacienteService.findById(id)
                .map(paciente -> ResponseEntity.ok(paciente))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> createPaciente(@Valid @RequestBody PacienteDTO pacienteDTO) {
        try {
            PacienteDTO savedPaciente = pacienteService.save(pacienteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPaciente);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> updatePaciente(@PathVariable Integer id,
                                                      @Valid @RequestBody PacienteDTO pacienteDTO) {
        try {
            return pacienteService.update(id, pacienteDTO)
                    .map(paciente -> ResponseEntity.ok(paciente))
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Integer id) {
        if (pacienteService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PacienteDTO>> getPacientesByUsuario(@PathVariable Integer usuarioId) {
        List<PacienteDTO> pacientes = pacienteService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(pacientes);
    }
}
