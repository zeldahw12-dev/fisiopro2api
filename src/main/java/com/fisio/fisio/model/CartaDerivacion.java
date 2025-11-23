package com.fisio.fisio.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cartas_derivacion")
public class CartaDerivacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carta")
    private Long idCarta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Blindaje a nivel BD
    private Paciente paciente;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 5)
    private String hora;

    private String remitente;
    private String cedula;

    private String destinatarioNombre;
    private String destinatarioEspecialidad;
    private String clinicaDestino;

    @Column(columnDefinition = "TEXT")
    private String motivoDerivacion;

    @Column(columnDefinition = "TEXT")
    private String resumenCaso;

    @Column(columnDefinition = "TEXT")
    private String hallazgosRelevantes;

    @Column(columnDefinition = "TEXT")
    private String pruebasRealizadas;

    @Column(columnDefinition = "TEXT")
    private String tratamientosIntentados;

    @Column(columnDefinition = "TEXT")
    private String respuestaTratamiento;

    @Column(columnDefinition = "TEXT")
    private String riesgosBanderasRojas;

    @Column(columnDefinition = "TEXT")
    private String medicamentosActuales;

    @Column(columnDefinition = "TEXT")
    private String alergias;

    @Column(columnDefinition = "TEXT")
    private String recomendaciones;

    @Column(columnDefinition = "TEXT")
    private String objetivoDerivacion;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Prioridad prioridad = Prioridad.MEDIA;

    private String contactoRemitente;
    private String contactoPaciente;

    @ElementCollection
    @CollectionTable(
            name = "carta_derivacion_adjuntos",
            joinColumns = @JoinColumn(name = "id_carta")
    )
    @Column(name = "url", length = 1000)
    private List<String> adjuntosLinks = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    /* ================== GETTERS / SETTERS ================== */

    public Long getIdCarta() { return idCarta; }
    public void setIdCarta(Long idCarta) { this.idCarta = idCarta; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    /** Paciente usa Integer */
    public Integer getIdPaciente() {
        return (paciente != null ? paciente.getIdPaciente() : null);
    }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getRemitente() { return remitente; }
    public void setRemitente(String remitente) { this.remitente = remitente; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getDestinatarioNombre() { return destinatarioNombre; }
    public void setDestinatarioNombre(String destinatarioNombre) { this.destinatarioNombre = destinatarioNombre; }

    public String getDestinatarioEspecialidad() { return destinatarioEspecialidad; }
    public void setDestinatarioEspecialidad(String destinatarioEspecialidad) { this.destinatarioEspecialidad = destinatarioEspecialidad; }

    public String getClinicaDestino() { return clinicaDestino; }
    public void setClinicaDestino(String clinicaDestino) { this.clinicaDestino = clinicaDestino; }

    public String getMotivoDerivacion() { return motivoDerivacion; }
    public void setMotivoDerivacion(String motivoDerivacion) { this.motivoDerivacion = motivoDerivacion; }

    public String getResumenCaso() { return resumenCaso; }
    public void setResumenCaso(String resumenCaso) { this.resumenCaso = resumenCaso; }

    public String getHallazgosRelevantes() { return hallazgosRelevantes; }
    public void setHallazgosRelevantes(String hallazgosRelevantes) { this.hallazgosRelevantes = hallazgosRelevantes; }

    public String getPruebasRealizadas() { return pruebasRealizadas; }
    public void setPruebasRealizadas(String pruebasRealizadas) { this.pruebasRealizadas = pruebasRealizadas; }

    public String getTratamientosIntentados() { return tratamientosIntentados; }
    public void setTratamientosIntentados(String tratamientosIntentados) { this.tratamientosIntentados = tratamientosIntentados; }

    public String getRespuestaTratamiento() { return respuestaTratamiento; }
    public void setRespuestaTratamiento(String respuestaTratamiento) { this.respuestaTratamiento = respuestaTratamiento; }

    public String getRiesgosBanderasRojas() { return riesgosBanderasRojas; }
    public void setRiesgosBanderasRojas(String riesgosBanderasRojas) { this.riesgosBanderasRojas = riesgosBanderasRojas; }

    public String getMedicamentosActuales() { return medicamentosActuales; }
    public void setMedicamentosActuales(String medicamentosActuales) { this.medicamentosActuales = medicamentosActuales; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(String recomendaciones) { this.recomendaciones = recomendaciones; }

    public String getObjetivoDerivacion() { return objetivoDerivacion; }
    public void setObjetivoDerivacion(String objetivoDerivacion) { this.objetivoDerivacion = objetivoDerivacion; }

    public Prioridad getPrioridad() { return prioridad; }
    public void setPrioridad(Prioridad prioridad) { this.prioridad = prioridad; }

    public String getContactoRemitente() { return contactoRemitente; }
    public void setContactoRemitente(String contactoRemitente) { this.contactoRemitente = contactoRemitente; }

    public String getContactoPaciente() { return contactoPaciente; }
    public void setContactoPaciente(String contactoPaciente) { this.contactoPaciente = contactoPaciente; }

    public List<String> getAdjuntosLinks() { return adjuntosLinks; }
    public void setAdjuntosLinks(List<String> adjuntosLinks) {
        this.adjuntosLinks = (adjuntosLinks != null ? adjuntosLinks : new ArrayList<>());
    }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
