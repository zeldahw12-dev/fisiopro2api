package com.fisio.fisio.model;

import com.fisio.fisio.model.enums.Sexo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "alta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alta")
    private Integer idAlta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Paciente paciente;

    @Column(name = "numero_expediente", length = 100)
    private String numeroExpediente;

    @Column(name = "nombre_paciente", length = 120)
    private String nombrePaciente;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", length = 10)
    private Sexo sexo;

    @Column(name = "diagnostico_final", length = 500)
    private String diagnosticoFinal;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Column(name = "fecha_alta")
    private LocalDate fechaAlta;

    @Column(name = "secuelas", length = 1000)
    private String secuelas;

    @Column(name = "motivo_alta", length = 500)
    private String motivoAlta;

    @Column(name = "servicios_otorgados", length = 1000)
    private String serviciosOtorgados;

    @Column(name = "nombre_terapeuta", length = 120)
    private String nombreTerapeuta;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Getters/Setters explícitos para compatibilidad con AltaService
    public Integer getIdAlta() { return idAlta; }
    public void setIdAlta(Integer idAlta) { this.idAlta = idAlta; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

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

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}