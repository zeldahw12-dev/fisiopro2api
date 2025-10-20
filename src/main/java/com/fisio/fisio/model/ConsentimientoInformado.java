// src/main/java/com/fisio/fisio/model/ConsentimientoInformado.java
package com.fisio.fisio.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "consentimientos")
public class ConsentimientoInformado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consentimiento")
    private Long idConsentimiento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Blindaje a nivel BD
    private Paciente paciente;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 5)
    private String hora;

    @Column(columnDefinition = "TEXT")
    private String procedimiento;

    @Column(columnDefinition = "TEXT")
    private String riesgos;

    @Column(columnDefinition = "TEXT")
    private String beneficios;

    @Column(columnDefinition = "TEXT")
    private String alternativas;

    @Column(columnDefinition = "TEXT")
    private String responsabilidadesPaciente;

    @Column(columnDefinition = "TEXT")
    private String responsabilidadesProfesional;

    @Column(columnDefinition = "TEXT")
    private String notasAdicionales;

    private Boolean preguntasResueltas = Boolean.TRUE;

    private String testigoNombre;
    private String testigoContacto;

    /* Getters / Setters */
    public Long getIdConsentimiento() { return idConsentimiento; }
    public void setIdConsentimiento(Long id) { this.idConsentimiento = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente p) { this.paciente = p; }

    public Integer getIdPaciente() { return paciente != null ? paciente.getIdPaciente() : null; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getProcedimiento() { return procedimiento; }
    public void setProcedimiento(String procedimiento) { this.procedimiento = procedimiento; }

    public String getRiesgos() { return riesgos; }
    public void setRiesgos(String riesgos) { this.riesgos = riesgos; }

    public String getBeneficios() { return beneficios; }
    public void setBeneficios(String beneficios) { this.beneficios = beneficios; }

    public String getAlternativas() { return alternativas; }
    public void setAlternativas(String alternativas) { this.alternativas = alternativas; }

    public String getResponsabilidadesPaciente() { return responsabilidadesPaciente; }
    public void setResponsabilidadesPaciente(String r) { this.responsabilidadesPaciente = r; }

    public String getResponsabilidadesProfesional() { return responsabilidadesProfesional; }
    public void setResponsabilidadesProfesional(String r) { this.responsabilidadesProfesional = r; }

    public String getNotasAdicionales() { return notasAdicionales; }
    public void setNotasAdicionales(String n) { this.notasAdicionales = n; }

    public Boolean getPreguntasResueltas() { return preguntasResueltas; }
    public void setPreguntasResueltas(Boolean v) { this.preguntasResueltas = v; }

    public String getTestigoNombre() { return testigoNombre; }
    public void setTestigoNombre(String t) { this.testigoNombre = t; }

    public String getTestigoContacto() { return testigoContacto; }
    public void setTestigoContacto(String t) { this.testigoContacto = t; }
}
