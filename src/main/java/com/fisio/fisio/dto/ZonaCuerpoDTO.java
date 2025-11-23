package com.fisio.fisio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonaCuerpoDTO {

    private Integer idZonaCuerpo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La URL de la foto no puede exceder 255 caracteres")
    private String foto;

    // Getters y Setters explícitos
    public Integer getIdZonaCuerpo() { return idZonaCuerpo; }
    public void setIdZonaCuerpo(Integer idZonaCuerpo) { this.idZonaCuerpo = idZonaCuerpo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
}
