package com.fisio.fisio.controller;

import com.fisio.fisio.dto.CitaDTO;
import com.fisio.fisio.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping
    public ResponseEntity<List<CitaDTO>> getAllCitas() {
        List<CitaDTO> citas = citaService.findAll();
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> getCitaById(@PathVariable Integer id) {
        return citaService.findById(id)
                .map(cita -> ResponseEntity.ok(cita))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CitaDTO> createCita(@Valid @RequestBody CitaDTO citaDTO) {
        CitaDTO savedCita = citaService.save(citaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCita);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaDTO> updateCita(@PathVariable Integer id,
                                              @Valid @RequestBody CitaDTO citaDTO) {
        return citaService.update(id, citaDTO)
                .map(cita -> ResponseEntity.ok(cita))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Integer id) {
        if (citaService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<CitaDTO>> getCitasByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<CitaDTO> citas = citaService.findByFechaBetween(fechaInicio, fechaFin);
        return ResponseEntity.ok(citas);
    }

    // Nuevo endpoint para obtener citas por paciente
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<CitaDTO>> getCitasByPaciente(@PathVariable Integer idPaciente) {
        List<CitaDTO> citas = citaService.findByPacienteId(idPaciente);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<CitaDTO>> getCitasByUsuario(@PathVariable Integer idUsuario) {
        List<CitaDTO> citas = citaService.findByUsuarioId(idUsuario);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/usuario/{idUsuario}/fecha")
    public ResponseEntity<List<CitaDTO>> getCitasByUsuarioAndFecha(
            @PathVariable Integer idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<CitaDTO> citas = citaService.findByUsuarioIdAndFechaBetween(idUsuario, fechaInicio, fechaFin);
        return ResponseEntity.ok(citas);
    }
}
