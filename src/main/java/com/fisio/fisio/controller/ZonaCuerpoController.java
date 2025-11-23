package com.fisio.fisio.controller;

import com.fisio.fisio.dto.ZonaCuerpoDTO;
import com.fisio.fisio.service.ZonaCuerpoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zonas-cuerpo")
@CrossOrigin(origins = "*")
public class ZonaCuerpoController {
    
    @Autowired
    private ZonaCuerpoService zonaCuerpoService;
    
    @GetMapping
    public ResponseEntity<List<ZonaCuerpoDTO>> getAllZonasCuerpo() {
        List<ZonaCuerpoDTO> zonasCuerpo = zonaCuerpoService.findAll();
        return ResponseEntity.ok(zonasCuerpo);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ZonaCuerpoDTO> getZonaCuerpoById(@PathVariable Integer id) {
        return zonaCuerpoService.findById(id)
                .map(zonaCuerpo -> ResponseEntity.ok(zonaCuerpo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ZonaCuerpoDTO> createZonaCuerpo(@Valid @RequestBody ZonaCuerpoDTO zonaCuerpoDTO) {
        ZonaCuerpoDTO savedZonaCuerpo = zonaCuerpoService.save(zonaCuerpoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedZonaCuerpo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ZonaCuerpoDTO> updateZonaCuerpo(@PathVariable Integer id, 
                                                         @Valid @RequestBody ZonaCuerpoDTO zonaCuerpoDTO) {
        return zonaCuerpoService.update(id, zonaCuerpoDTO)
                .map(zonaCuerpo -> ResponseEntity.ok(zonaCuerpo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZonaCuerpo(@PathVariable Integer id) {
        if (zonaCuerpoService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ZonaCuerpoDTO> getZonaCuerpoByNombre(@PathVariable String nombre) {
        return zonaCuerpoService.findByNombre(nombre)
                .map(zonaCuerpo -> ResponseEntity.ok(zonaCuerpo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<ZonaCuerpoDTO>> getZonasCuerpoByNombre(@RequestParam String nombre) {
        List<ZonaCuerpoDTO> zonasCuerpo = zonaCuerpoService.findByNombreContaining(nombre);
        return ResponseEntity.ok(zonasCuerpo);
    }
}
