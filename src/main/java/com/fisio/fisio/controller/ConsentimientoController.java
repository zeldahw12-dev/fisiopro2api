// src/main/java/com/fisio/fisio/controller/ConsentimientoController.java
package com.fisio.fisio.controller;

import com.fisio.fisio.dto.ConsentimientoDTO;
import com.fisio.fisio.service.ConsentimientoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consentimientos")
@CrossOrigin(origins = "*")
public class ConsentimientoController {

    private final ConsentimientoService service;
    public ConsentimientoController(ConsentimientoService service) { this.service = service; }

    @GetMapping("/{id}")
    public ResponseEntity<ConsentimientoDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<ConsentimientoDTO>> byPaciente(@PathVariable Integer idPaciente) {
        return ResponseEntity.ok(service.getByPacienteId(idPaciente));
    }

    @PostMapping
    public ResponseEntity<ConsentimientoDTO> create(@Valid @RequestBody ConsentimientoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsentimientoDTO> update(@PathVariable Long id, @Valid @RequestBody ConsentimientoDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
