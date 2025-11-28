package com.fisio.fisio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO de salida (lectura). Incluye 'contra' para no romper tu app actual.
 * Idealmente NO debería exponerse la contraseña, pero lo mantenemos por compatibilidad.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Integer idUsuario;

    private String nickname;

    private String nombre;

    // <CHANGE> Replaced edad with fechaNacimiento
    private LocalDate fechaNacimiento;

    private String email;

    private String contra;

    private String foto;

    private String profesion;

    // Getters/Setters explícitos opcionales
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // <CHANGE> Replaced getEdad/setEdad with fechaNacimiento methods
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContra() { return contra; }
    public void setContra(String contra) { this.contra = contra; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    public String getProfesion() { return profesion; }
    public void setProfesion(String profesion) { this.profesion = profesion; }
}