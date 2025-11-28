package com.fisio.fisio.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {

    private Integer idPaciente;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    // <CHANGE> Replaced edad with fechaNacimiento
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @Size(max = 255, message = "El diagnóstico médico no puede exceder 255 caracteres")
    private String diagnosticoMedico;

    @NotNull(message = "El usuario es obligatorio")
    private Integer usuarioId;

    // Getters y Setters explícitos
    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // <CHANGE> Replaced getEdad/setEdad with fechaNacimiento methods
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getDiagnosticoMedico() { return diagnosticoMedico; }
    public void setDiagnosticoMedico(String diagnosticoMedico) { this.diagnosticoMedico = diagnosticoMedico; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
}