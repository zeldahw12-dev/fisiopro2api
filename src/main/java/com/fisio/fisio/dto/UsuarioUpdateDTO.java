package com.fisio.fisio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de actualización parcial.
 * Todos los campos son opcionales. Solo se actualizan los que vengan NO nulos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {

    @Size(max = 80, message = "El nickname no puede exceder 80 caracteres")
    private String nickname;

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Min(value = 1, message = "La edad debe ser mayor a 0")
    @Max(value = 120, message = "La edad debe ser menor a 120")
    private Integer edad;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    // Contraseña opcional en update general (si viene, se asigna)
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String contra;

    @Size(max = 500, message = "La URL de la foto no puede exceder 500 caracteres")
    private String foto;

    @Size(max = 120, message = "La profesión no puede exceder 120 caracteres")
    private String profesion;

    public @Size(max = 80, message = "El nickname no puede exceder 80 caracteres") String getNickname() {
        return nickname;
    }

    public void setNickname(@Size(max = 80, message = "El nickname no puede exceder 80 caracteres") String nickname) {
        this.nickname = nickname;
    }

    public @Size(max = 100, message = "El nombre no puede exceder 100 caracteres") String getNombre() {
        return nombre;
    }

    public void setNombre(@Size(max = 100, message = "El nombre no puede exceder 100 caracteres") String nombre) {
        this.nombre = nombre;
    }

    public @Min(value = 1, message = "La edad debe ser mayor a 0") @Max(value = 120, message = "La edad debe ser menor a 120") Integer getEdad() {
        return edad;
    }

    public void setEdad(@Min(value = 1, message = "La edad debe ser mayor a 0") @Max(value = 120, message = "La edad debe ser menor a 120") Integer edad) {
        this.edad = edad;
    }

    public @Email(message = "El email debe tener un formato válido") @Size(max = 100, message = "El email no puede exceder 100 caracteres") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "El email debe tener un formato válido") @Size(max = 100, message = "El email no puede exceder 100 caracteres") String email) {
        this.email = email;
    }

    public @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres") String getContra() {
        return contra;
    }

    public void setContra(@Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres") String contra) {
        this.contra = contra;
    }

    public @Size(max = 500, message = "La URL de la foto no puede exceder 500 caracteres") String getFoto() {
        return foto;
    }

    public void setFoto(@Size(max = 500, message = "La URL de la foto no puede exceder 500 caracteres") String foto) {
        this.foto = foto;
    }

    public @Size(max = 120, message = "La profesión no puede exceder 120 caracteres") String getProfesion() {
        return profesion;
    }

    public void setProfesion(@Size(max = 120, message = "La profesión no puede exceder 120 caracteres") String profesion) {
        this.profesion = profesion;
    }
}
