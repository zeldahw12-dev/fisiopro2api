package com.fisio.fisio.controller;

import com.fisio.fisio.dto.AgendaDTO;
import com.fisio.fisio.dto.PacienteInfoCompletaDTO;
import com.fisio.fisio.service.AgendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendas") // Cambiado de "/agenda" a "/agendas"
@CrossOrigin(origins = "*")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;
    @GetMapping("/paciente/{pacienteId}/info-completa")
    public ResponseEntity<PacienteInfoCompletaDTO> getInfoCompletaPaciente(@PathVariable Integer pacienteId) {
        try {
            return agendaService.obtenerInfoCompletaPaciente(pacienteId)
                    .map(info -> ResponseEntity.ok(info))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping
    public ResponseEntity<List<AgendaDTO>> getAllAgendas() {
        List<AgendaDTO> agendas = agendaService.findAll();
        return ResponseEntity.ok(agendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendaDTO> getAgendaById(@PathVariable Integer id) {
        return agendaService.findById(id)
                .map(agenda -> ResponseEntity.ok(agenda))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAgenda(@Valid @RequestBody AgendaDTO agendaDTO) {
        try {
            AgendaDTO savedAgenda = agendaService.save(agendaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAgenda);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAgenda(@PathVariable Integer id,
                                          @Valid @RequestBody AgendaDTO agendaDTO) {
        try {
            return agendaService.update(id, agendaDTO)
                    .map(agenda -> ResponseEntity.ok(agenda))
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgenda(@PathVariable Integer id) {
        if (agendaService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<AgendaDTO>> getAgendasByPaciente(@PathVariable Integer pacienteId) {
        List<AgendaDTO> agendas = agendaService.findByPacienteId(pacienteId);
        return ResponseEntity.ok(agendas);
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<List<AgendaDTO>> getAgendasByCita(@PathVariable Integer citaId) {
        List<AgendaDTO> agendas = agendaService.findByCitaId(citaId);
        return ResponseEntity.ok(agendas);
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<AgendaDTO>> getAgendasByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<AgendaDTO> agendas = agendaService.findByFechaBetween(fechaInicio, fechaFin);
        return ResponseEntity.ok(agendas);
    }
}
