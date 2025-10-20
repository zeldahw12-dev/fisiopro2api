package com.fisio.fisio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "agenda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idagenda")
    private Integer idAgenda;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "intervencion", nullable = false, length = 200)
    private String intervencion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cita", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cita cita;

    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<NotaEvolucion> notasEvolucion;

    public Integer getIdAgenda() { return idAgenda; }
    public void setIdAgenda(Integer idAgenda) { this.idAgenda = idAgenda; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getIntervencion() { return intervencion; }
    public void setIntervencion(String intervencion) { this.intervencion = intervencion; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
    public List<NotaEvolucion> getNotasEvolucion() { return notasEvolucion; }
    public void setNotasEvolucion(List<NotaEvolucion> notasEvolucion) { this.notasEvolucion = notasEvolucion; }
}
