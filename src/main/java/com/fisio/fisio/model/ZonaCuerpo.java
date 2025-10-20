package com.fisio.fisio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "zonaCuerpo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonaCuerpo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idzonaCuerpo")
    private Integer idZonaCuerpo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "foto", length = 255)
    private String foto;

    @OneToMany(mappedBy = "zonaCuerpo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NotaEvolucion> notasEvolucion;

    // Getters y Setters explícitos
    public Integer getIdZonaCuerpo() { return idZonaCuerpo; }
    public void setIdZonaCuerpo(Integer idZonaCuerpo) { this.idZonaCuerpo = idZonaCuerpo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public List<NotaEvolucion> getNotasEvolucion() { return notasEvolucion; }
    public void setNotasEvolucion(List<NotaEvolucion> notasEvolucion) { this.notasEvolucion = notasEvolucion; }
}
