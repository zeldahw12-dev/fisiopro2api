package com.fisio.fisio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "hoja_ejercicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HojaEjercicios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idhoja")
    private Integer idHoja;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Blindaje a nivel BD
    private Paciente paciente;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    /** Texto libre donde el fisio escribe la rutina tal cual */
    @Lob
    @Column(name = "ejercicios_texto", columnDefinition = "TEXT")
    private String ejerciciosTexto;

    /** Objetivos/indicaciones generales (opcional) */
    @Lob
    @Column(name = "objetivos", columnDefinition = "TEXT")
    private String objetivos;

    /** Recomendaciones finales (opcional) */
    @Lob
    @Column(name = "recomendaciones", columnDefinition = "TEXT")
    private String recomendaciones;

    /**
     * Varias URLs de video separadas por salto de línea.
     * En el DTO se maneja como List<String>.
     */
    @Lob
    @Column(name = "video_urls", columnDefinition = "TEXT")
    private String videoUrls;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Getters / Setters explícitos si los necesitas además de Lombok:

    public Integer getIdHoja() { return idHoja; }
    public void setIdHoja(Integer idHoja) { this.idHoja = idHoja; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getEjerciciosTexto() { return ejerciciosTexto; }
    public void setEjerciciosTexto(String ejerciciosTexto) { this.ejerciciosTexto = ejerciciosTexto; }

    public String getObjetivos() { return objetivos; }
    public void setObjetivos(String objetivos) { this.objetivos = objetivos; }

    public String getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(String recomendaciones) { this.recomendaciones = recomendaciones; }

    public String getVideoUrls() { return videoUrls; }
    public void setVideoUrls(String videoUrls) { this.videoUrls = videoUrls; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
