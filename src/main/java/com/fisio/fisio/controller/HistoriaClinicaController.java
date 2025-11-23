package com.fisio.fisio.controller;

import com.fisio.fisio.dto.HistoriaClinicaDTO;
import com.fisio.fisio.service.HistoriaClinicaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/historias-clinicas")
@CrossOrigin(origins = "*")
public class HistoriaClinicaController {

    @Autowired
    private HistoriaClinicaService historiaClinicaService;

    @GetMapping
    public ResponseEntity<List<HistoriaClinicaDTO>> obtenerTodasLasHistorias() {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.obtenerTodasLasHistorias();
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriaClinicaDTO> obtenerHistoriaPorId(@PathVariable Integer id) {
        try {
            Optional<HistoriaClinicaDTO> historia = historiaClinicaService.obtenerHistoriaPorId(id);
            return historia.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<HistoriaClinicaDTO>> obtenerHistoriasPorPaciente(@PathVariable Integer idPaciente) {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.obtenerHistoriasPorPaciente(idPaciente);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paciente/{idPaciente}/ordenadas")
    public ResponseEntity<List<HistoriaClinicaDTO>> obtenerHistoriasPorPacienteOrdenadas(@PathVariable Integer idPaciente) {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.obtenerHistoriasPorPacienteOrdenadas(idPaciente);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<HistoriaClinicaDTO>> buscarPorNombre(@RequestParam String nombre) {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.buscarPorNombre(nombre);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar/fechas")
    public ResponseEntity<List<HistoriaClinicaDTO>> buscarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.buscarPorFechas(fechaInicio, fechaFin);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar/fisioterapeuta")
    public ResponseEntity<List<HistoriaClinicaDTO>> buscarPorFisioterapeuta(@RequestParam String fisioterapeuta) {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.buscarPorFisioterapeuta(fisioterapeuta);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar/medico-tratante")
    public ResponseEntity<List<HistoriaClinicaDTO>> buscarPorMedicoTratante(@RequestParam String medicoTratante) {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.buscarPorMedicoTratante(medicoTratante);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar/diagnostico-medico")
    public ResponseEntity<List<HistoriaClinicaDTO>> buscarPorDiagnosticoMedico(@RequestParam String diagnostico) {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.buscarPorDiagnosticoMedico(diagnostico);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar/diagnostico-fisioterapeutico")
    public ResponseEntity<List<HistoriaClinicaDTO>> buscarPorDiagnosticoFisioterapeutico(@RequestParam String diagnostico) {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.buscarPorDiagnosticoFisioterapeutico(diagnostico);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar/escolaridad")
    public ResponseEntity<List<HistoriaClinicaDTO>> buscarPorEscolaridad(@RequestParam String escolaridad) {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.buscarPorEscolaridad(escolaridad);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar/padecimiento-actual")
    public ResponseEntity<List<HistoriaClinicaDTO>> buscarPorPadecimientoActual(@RequestParam String padecimiento) {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.buscarPorPadecimientoActual(padecimiento);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar/pruebas-especiales")
    public ResponseEntity<List<HistoriaClinicaDTO>> buscarPorPruebasEspeciales(@RequestParam String pruebas) {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.buscarPorPruebasEspeciales(pruebas);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/contar/paciente/{idPaciente}")
    public ResponseEntity<Long> contarHistoriasPorPaciente(@PathVariable Integer idPaciente) {
        try {
            Long cantidad = historiaClinicaService.contarHistoriasPorPaciente(idPaciente);
            return ResponseEntity.ok(cantidad);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<HistoriaClinicaDTO> crearHistoria(@Valid @RequestBody HistoriaClinicaDTO historiaDTO) {
        try {
            HistoriaClinicaDTO nuevaHistoria = historiaClinicaService.crearHistoria(historiaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaHistoria);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriaClinicaDTO> actualizarHistoria(
            @PathVariable Integer id,
            @Valid @RequestBody HistoriaClinicaDTO historiaDTO) {
        try {
            HistoriaClinicaDTO historiaActualizada = historiaClinicaService.actualizarHistoria(id, historiaDTO);
            return ResponseEntity.ok(historiaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHistoria(@PathVariable Integer id) {
        try {
            historiaClinicaService.eliminarHistoria(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
