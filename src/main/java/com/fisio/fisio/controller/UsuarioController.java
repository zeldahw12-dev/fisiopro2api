package com.fisio.fisio.controller;

import com.fisio.fisio.dto.UsuarioCreateDTO;
import com.fisio.fisio.dto.UsuarioDTO;
import com.fisio.fisio.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /* ====== GET ====== */

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Integer id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> getUsuarioByEmail(@PathVariable String email) {
        return usuarioService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* ====== DASHBOARD (nuevo GET visual) ====== */
    @GetMapping("/{id}/dashboard")
    public ResponseEntity<?> getDashboard(@PathVariable Integer id) {
        return usuarioService.buildDashboard(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* ====== POST (CREATE) ====== */

    @PostMapping
    public ResponseEntity<?> createUsuario(@Valid @RequestBody UsuarioCreateDTO body) {
        if (usuarioService.existsByEmail(body.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El email ya está registrado");
        }
        if (usuarioService.existsByNickname(body.getNickname())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El nickname ya está en uso");
        }
        UsuarioDTO saved = usuarioService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /* ====== PUT / PATCH (UPDATE PARCIAL usando UsuarioDTO) ====== */

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuarioPut(@PathVariable Integer id,
                                                       @Valid @RequestBody UsuarioDTO body) {
        return usuarioService.update(id, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuarioPatch(@PathVariable Integer id,
                                                         @Valid @RequestBody UsuarioDTO body) {
        return usuarioService.update(id, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* ====== PATCH PASSWORD ====== */

    public static class PasswordBody {
        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
        public String contra;
        public String getContra() { return contra; }
        public void setContra(String contra) { this.contra = contra; }
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<UsuarioDTO> updatePassword(@PathVariable Integer id,
                                                     @Valid @RequestBody PasswordBody body) {
        return usuarioService.updatePassword(id, body.getContra())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* ====== DELETE ====== */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        if (usuarioService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
