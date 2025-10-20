package com.fisio.fisio.controller;

import com.fisio.fisio.dto.NotaEvolucionDTO;
import com.fisio.fisio.service.NotaEvolucionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notas-evolucion")
@CrossOrigin(origins = "*")
public class NotaEvolucionController {

    @Autowired
    private NotaEvolucionService notaEvolucionService;

    @GetMapping
    public ResponseEntity<List<NotaEvolucionDTO>> getAllNotasEvolucion() {
        List<NotaEvolucionDTO> notasEvolucion = notaEvolucionService.findAll();
        return ResponseEntity.ok(notasEvolucion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaEvolucionDTO> getNotaEvolucionById(@PathVariable Integer id) {
        return notaEvolucionService.findById(id)
                .map(notaEvolucion -> ResponseEntity.ok(notaEvolucion))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NotaEvolucionDTO> createNotaEvolucion(@Valid @RequestBody NotaEvolucionDTO notaEvolucionDTO) {
        try {
            NotaEvolucionDTO savedNotaEvolucion = notaEvolucionService.save(notaEvolucionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNotaEvolucion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaEvolucionDTO> updateNotaEvolucion(@PathVariable Integer id,
                                                                @Valid @RequestBody NotaEvolucionDTO notaEvolucionDTO) {
        try {
            return notaEvolucionService.update(id, notaEvolucionDTO)
                    .map(notaEvolucion -> ResponseEntity.ok(notaEvolucion))
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotaEvolucion(@PathVariable Integer id) {
        if (notaEvolucionService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/agenda/{agendaId}")
    public ResponseEntity<List<NotaEvolucionDTO>> getNotasEvolucionByAgenda(@PathVariable Integer agendaId) {
        List<NotaEvolucionDTO> notasEvolucion = notaEvolucionService.findByAgendaId(agendaId);
        return ResponseEntity.ok(notasEvolucion);
    }

    @GetMapping("/zona-cuerpo/{zonaCuerpoId}")
    public ResponseEntity<List<NotaEvolucionDTO>> getNotasEvolucionByZonaCuerpo(@PathVariable Integer zonaCuerpoId) {
        List<NotaEvolucionDTO> notasEvolucion = notaEvolucionService.findByZonaCuerpoId(zonaCuerpoId);
        return ResponseEntity.ok(notasEvolucion);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<NotaEvolucionDTO>> getNotasEvolucionByPaciente(@PathVariable Integer pacienteId) {
        List<NotaEvolucionDTO> notasEvolucion = notaEvolucionService.findByPacienteId(pacienteId);
        return ResponseEntity.ok(notasEvolucion);
    }
}
