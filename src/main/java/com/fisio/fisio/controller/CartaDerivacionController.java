package com.fisio.fisio.controller;

import com.fisio.fisio.dto.CartaDerivacionDTO;
import com.fisio.fisio.service.CartaDerivacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartas-derivacion")
@CrossOrigin(origins = "*")
public class CartaDerivacionController {

    private final CartaDerivacionService service;

    public CartaDerivacionController(CartaDerivacionService service) {
        this.service = service;
    }

    /** LISTAR por paciente */
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<CartaDerivacionDTO>> getByPaciente(@PathVariable Integer idPaciente) {
        return ResponseEntity.ok(service.obtenerPorPaciente(idPaciente));
    }

    /** OBTENER por idCarta */
    @GetMapping("/{id}")
    public ResponseEntity<CartaDerivacionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    /** CREAR */
    @PostMapping
    public ResponseEntity<CartaDerivacionDTO> create(@Valid @RequestBody CartaDerivacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    /** ACTUALIZAR */
    @PutMapping("/{id}")
    public ResponseEntity<CartaDerivacionDTO> update(@PathVariable Long id,
                                                     @Valid @RequestBody CartaDerivacionDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    /** ELIMINAR */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
