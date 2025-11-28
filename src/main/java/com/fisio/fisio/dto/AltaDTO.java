package com.fisio.fisio.dto;

import com.fisio.fisio.model.enums.Sexo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AltaDTO {
    private Integer idAlta;

    @Size(max = 50, message = "El número de expediente no puede exceder 50 caracteres")
    private String numeroExpediente;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(max = 100, message = "El nombre del paciente no puede exceder 100 caracteres")
    private String nombrePaciente;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    private Sexo sexo;

    private String diagnosticoFinal;

    private LocalDate fechaIngreso;

    @NotNull(message = "La fecha de alta es obligatoria")
    private LocalDate fechaAlta;

    private String secuelas;

    private String motivoAlta;

    private String serviciosOtorgados;

    @Size(max = 100, message = "El nombre del terapeuta no puede exceder 100 caracteres")
    private String nombreTerapeuta;

    private String observaciones;

    @NotNull(message = "El paciente es obligatorio")
    private Integer pacienteId;

    // Getters y Setters explícitos
    public Integer getIdAlta() { return idAlta; }
    public void setIdAlta(Integer idAlta) { this.idAlta = idAlta; }

    public String getNumeroExpediente() { return numeroExpediente; }
    public void setNumeroExpediente(String numeroExpediente) { this.numeroExpediente = numeroExpediente; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public String getDiagnosticoFinal() { return diagnosticoFinal; }
    public void setDiagnosticoFinal(String diagnosticoFinal) { this.diagnosticoFinal = diagnosticoFinal; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    public String getSecuelas() { return secuelas; }
    public void setSecuelas(String secuelas) { this.secuelas = secuelas; }

    public String getMotivoAlta() { return motivoAlta; }
    public void setMotivoAlta(String motivoAlta) { this.motivoAlta = motivoAlta; }

    public String getServiciosOtorgados() { return serviciosOtorgados; }
    public void setServiciosOtorgados(String serviciosOtorgados) { this.serviciosOtorgados = serviciosOtorgados; }

    public String getNombreTerapeuta() { return nombreTerapeuta; }
    public void setNombreTerapeuta(String nombreTerapeuta) { this.nombreTerapeuta = nombreTerapeuta; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Integer getPacienteId() { return pacienteId; }
    public void setPacienteId(Integer pacienteId) { this.pacienteId = pacienteId; }
}