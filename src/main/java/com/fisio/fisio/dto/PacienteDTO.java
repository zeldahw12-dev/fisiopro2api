package com.fisio.fisio.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {

    private Integer idPaciente;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 1, message = "La edad debe ser mayor a 0")
    @Max(value = 120, message = "La edad debe ser menor a 120")
    private Integer edad;

    @Size(max = 255, message = "El diagnóstico médico no puede exceder 255 caracteres")
    private String diagnosticoMedico;

    @NotNull(message = "El usuario es obligatorio")
    private Integer usuarioId;

    // Getters y Setters explícitos
    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getDiagnosticoMedico() { return diagnosticoMedico; }
    public void setDiagnosticoMedico(String diagnosticoMedico) { this.diagnosticoMedico = diagnosticoMedico; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
}
