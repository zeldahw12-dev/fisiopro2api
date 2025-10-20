package com.fisio.fisio.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HojaEjerciciosDTO {

    private Integer idHoja;

    @NotNull(message = "El paciente es obligatorio")
    private Integer pacienteId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede exceder 150 caracteres")
    private String titulo;

    /** Texto libre con la rutina (puede ser largo) */
    private String ejerciciosTexto;

    private String objetivos;

    private String recomendaciones;

    /**
     * Lista de URLs de video. El Service las convierte a String con saltos de línea
     * para persistir en la entidad.
     */
    private List<@Pattern(
            regexp = "^(https?://).+",
            message = "Cada URL de video debe comenzar con http:// o https://"
    ) String> videoUrls;

    // Getters y Setters explícitos (estilo consistente)
    public Integer getIdHoja() { return idHoja; }
    public void setIdHoja(Integer idHoja) { this.idHoja = idHoja; }

    public Integer getPacienteId() { return pacienteId; }
    public void setPacienteId(Integer pacienteId) { this.pacienteId = pacienteId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getEjerciciosTexto() { return ejerciciosTexto; }
    public void setEjerciciosTexto(String ejerciciosTexto) { this.ejerciciosTexto = ejerciciosTexto; }

    public String getObjetivos() { return objetivos; }
    public void setObjetivos(String objetivos) { this.objetivos = objetivos; }

    public String getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(String recomendaciones) { this.recomendaciones = recomendaciones; }

    public List<String> getVideoUrls() { return videoUrls; }
    public void setVideoUrls(List<String> videoUrls) { this.videoUrls = videoUrls; }
}
