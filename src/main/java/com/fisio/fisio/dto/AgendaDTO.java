package com.fisio.fisio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaDTO {
    private Integer idAgenda;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;

    @NotNull(message = "La intervención es obligatoria")
    @Size(max = 200, message = "La intervención no puede exceder 200 caracteres")
    private String intervencion;

    private String observaciones;

    @NotNull(message = "El paciente es obligatorio")
    private Integer pacienteId;

    @NotNull(message = "La cita es obligatoria")
    private Integer citaId;

    // Campo adicional para mostrar información en el frontend
    private String nombrePaciente;

    // Getters y Setters explícitos para asegurar compatibilidad
    public Integer getIdAgenda() { return idAgenda; }
    public void setIdAgenda(Integer idAgenda) { this.idAgenda = idAgenda; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getIntervencion() { return intervencion; }
    public void setIntervencion(String intervencion) { this.intervencion = intervencion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Integer getPacienteId() { return pacienteId; }
    public void setPacienteId(Integer pacienteId) { this.pacienteId = pacienteId; }

    public Integer getCitaId() { return citaId; }
    public void setCitaId(Integer citaId) { this.citaId = citaId; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }
}
