// src/main/java/com/fisio/fisio/dto/ConsentimientoDTO.java
package com.fisio.fisio.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ConsentimientoDTO {
    private Integer idConsentimiento;      // mapeamos Long -> Integer en el service
    @NotNull private Integer idPaciente;
    @NotNull private LocalDate fecha;
    private String hora;

    private String procedimiento;
    private String riesgos;
    private String beneficios;
    private String alternativas;
    private String responsabilidadesPaciente;
    private String responsabilidadesProfesional;
    private String notasAdicionales;
    private Boolean preguntasResueltas;
    private String testigoNombre;
    private String testigoContacto;

    public Integer getIdConsentimiento() {
        return idConsentimiento;
    }

    public void setIdConsentimiento(Integer idConsentimiento) {
        this.idConsentimiento = idConsentimiento;
    }

    public @NotNull Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(@NotNull Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public @NotNull LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(@NotNull LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getRiesgos() {
        return riesgos;
    }

    public void setRiesgos(String riesgos) {
        this.riesgos = riesgos;
    }

    public String getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }

    public String getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(String alternativas) {
        this.alternativas = alternativas;
    }

    public String getResponsabilidadesPaciente() {
        return responsabilidadesPaciente;
    }

    public void setResponsabilidadesPaciente(String responsabilidadesPaciente) {
        this.responsabilidadesPaciente = responsabilidadesPaciente;
    }

    public String getResponsabilidadesProfesional() {
        return responsabilidadesProfesional;
    }

    public void setResponsabilidadesProfesional(String responsabilidadesProfesional) {
        this.responsabilidadesProfesional = responsabilidadesProfesional;
    }

    public String getNotasAdicionales() {
        return notasAdicionales;
    }

    public void setNotasAdicionales(String notasAdicionales) {
        this.notasAdicionales = notasAdicionales;
    }

    public Boolean getPreguntasResueltas() {
        return preguntasResueltas;
    }

    public void setPreguntasResueltas(Boolean preguntasResueltas) {
        this.preguntasResueltas = preguntasResueltas;
    }

    public String getTestigoNombre() {
        return testigoNombre;
    }

    public void setTestigoNombre(String testigoNombre) {
        this.testigoNombre = testigoNombre;
    }

    public String getTestigoContacto() {
        return testigoContacto;
    }

    public void setTestigoContacto(String testigoContacto) {
        this.testigoContacto = testigoContacto;
    }
}
