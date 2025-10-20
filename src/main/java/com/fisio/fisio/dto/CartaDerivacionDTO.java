// src/main/java/com/fisio/fisio/dto/CartaDerivacionDTO.java
package com.fisio.fisio.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartaDerivacionDTO {

    private Integer idCarta;

    @NotNull(message = "El id del paciente es obligatorio")
    private Integer idPaciente;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private String hora;

    // Remitente (fisioterapeuta)
    private String remitente;
    private String cedula;

    // Destinatario
    private String destinatarioNombre;
    private String destinatarioEspecialidad;
    private String clinicaDestino;

    // Contenido
    private String motivoDerivacion;
    private String resumenCaso;
    private String hallazgosRelevantes;
    private String pruebasRealizadas;
    private String tratamientosIntentados;
    private String respuestaTratamiento;
    private String riesgosBanderasRojas;
    private String medicamentosActuales;
    private String alergias;
    private String recomendaciones;
    private String objetivoDerivacion;

    @Pattern(regexp = "BAJA|MEDIA|ALTA", message = "La prioridad debe ser BAJA, MEDIA o ALTA")
    private String prioridad;

    private String contactoRemitente;
    private String contactoPaciente;

    private List<@Size(max = 512) String> adjuntosLinks;

    private String observaciones;

    public Integer getIdCarta() {
        return idCarta;
    }

    public void setIdCarta(Integer idCarta) {
        this.idCarta = idCarta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public @NotNull(message = "El id del paciente es obligatorio") Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(@NotNull(message = "El id del paciente es obligatorio") Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public List<@Size(max = 512) String> getAdjuntosLinks() {
        return adjuntosLinks;
    }

    public void setAdjuntosLinks(List<@Size(max = 512) String> adjuntosLinks) {
        this.adjuntosLinks = adjuntosLinks;
    }

    public String getContactoPaciente() {
        return contactoPaciente;
    }

    public void setContactoPaciente(String contactoPaciente) {
        this.contactoPaciente = contactoPaciente;
    }

    public String getMedicamentosActuales() {
        return medicamentosActuales;
    }

    public void setMedicamentosActuales(String medicamentosActuales) {
        this.medicamentosActuales = medicamentosActuales;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public String getObjetivoDerivacion() {
        return objetivoDerivacion;
    }

    public void setObjetivoDerivacion(String objetivoDerivacion) {
        this.objetivoDerivacion = objetivoDerivacion;
    }

    public @Pattern(regexp = "BAJA|MEDIA|ALTA", message = "La prioridad debe ser BAJA, MEDIA o ALTA") String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(@Pattern(regexp = "BAJA|MEDIA|ALTA", message = "La prioridad debe ser BAJA, MEDIA o ALTA") String prioridad) {
        this.prioridad = prioridad;
    }

    public String getContactoRemitente() {
        return contactoRemitente;
    }

    public void setContactoRemitente(String contactoRemitente) {
        this.contactoRemitente = contactoRemitente;
    }

    public @NotNull(message = "La fecha es obligatoria") LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(@NotNull(message = "La fecha es obligatoria") LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDestinatarioNombre() {
        return destinatarioNombre;
    }

    public void setDestinatarioNombre(String destinatarioNombre) {
        this.destinatarioNombre = destinatarioNombre;
    }

    public String getClinicaDestino() {
        return clinicaDestino;
    }

    public void setClinicaDestino(String clinicaDestino) {
        this.clinicaDestino = clinicaDestino;
    }

    public String getDestinatarioEspecialidad() {
        return destinatarioEspecialidad;
    }

    public void setDestinatarioEspecialidad(String destinatarioEspecialidad) {
        this.destinatarioEspecialidad = destinatarioEspecialidad;
    }

    public String getMotivoDerivacion() {
        return motivoDerivacion;
    }

    public void setMotivoDerivacion(String motivoDerivacion) {
        this.motivoDerivacion = motivoDerivacion;
    }

    public String getResumenCaso() {
        return resumenCaso;
    }

    public void setResumenCaso(String resumenCaso) {
        this.resumenCaso = resumenCaso;
    }

    public String getHallazgosRelevantes() {
        return hallazgosRelevantes;
    }

    public void setHallazgosRelevantes(String hallazgosRelevantes) {
        this.hallazgosRelevantes = hallazgosRelevantes;
    }

    public String getPruebasRealizadas() {
        return pruebasRealizadas;
    }

    public void setPruebasRealizadas(String pruebasRealizadas) {
        this.pruebasRealizadas = pruebasRealizadas;
    }

    public String getTratamientosIntentados() {
        return tratamientosIntentados;
    }

    public void setTratamientosIntentados(String tratamientosIntentados) {
        this.tratamientosIntentados = tratamientosIntentados;
    }

    public String getRespuestaTratamiento() {
        return respuestaTratamiento;
    }

    public void setRespuestaTratamiento(String respuestaTratamiento) {
        this.respuestaTratamiento = respuestaTratamiento;
    }

    public String getRiesgosBanderasRojas() {
        return riesgosBanderasRojas;
    }

    public void setRiesgosBanderasRojas(String riesgosBanderasRojas) {
        this.riesgosBanderasRojas = riesgosBanderasRojas;
    }
}
