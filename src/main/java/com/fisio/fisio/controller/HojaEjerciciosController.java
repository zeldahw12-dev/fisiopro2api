package com.fisio.fisio.controller;

import com.fisio.fisio.dto.HojaEjerciciosDTO;
import com.fisio.fisio.service.HojaEjerciciosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hojas")
@CrossOrigin(origins = "*")
public class HojaEjerciciosController {

    @Autowired
    private HojaEjerciciosService hojaEjerciciosService;

    @GetMapping
    public ResponseEntity<List<HojaEjerciciosDTO>> getAll() {
        return ResponseEntity.ok(hojaEjerciciosService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HojaEjerciciosDTO> getById(@PathVariable Integer id) {
        return hojaEjerciciosService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<HojaEjerciciosDTO>> getByPaciente(@PathVariable Integer pacienteId) {
        return ResponseEntity.ok(hojaEjerciciosService.findByPacienteId(pacienteId));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<HojaEjerciciosDTO>> searchByTitulo(@RequestParam("q") String q) {
        return ResponseEntity.ok(hojaEjerciciosService.findByTituloLike(q));
    }

    @PostMapping
    public ResponseEntity<HojaEjerciciosDTO> create(@Valid @RequestBody HojaEjerciciosDTO dto) {
        try {
            HojaEjerciciosDTO saved = hojaEjerciciosService.save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HojaEjerciciosDTO> update(@PathVariable Integer id,
                                                    @Valid @RequestBody HojaEjerciciosDTO dto) {
        try {
            return hojaEjerciciosService.update(id, dto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (hojaEjerciciosService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
