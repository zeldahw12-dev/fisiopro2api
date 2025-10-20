package com.fisio.fisio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "paciente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpaciente")
    private Integer idPaciente;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name = "diagnostico_medico", length = 255)
    private String diagnosticoMedico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;

    // Ya existentes
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Agenda> agendas;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cita> citas;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HistoriaClinica> historiasClinicas;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Alta> altas;

    // NUEVO: enlaza cartas de derivación
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartaDerivacion> cartasDerivacion;

    // NUEVO: enlaza consentimientos
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ConsentimientoInformado> consentimientos;

    // NUEVO: enlaza hojas de ejercicios
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HojaEjercicios> hojasEjercicios;

    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public String getDiagnosticoMedico() { return diagnosticoMedico; }
    public void setDiagnosticoMedico(String diagnosticoMedico) { this.diagnosticoMedico = diagnosticoMedico; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<Agenda> getAgendas() { return agendas; }
    public void setAgendas(List<Agenda> agendas) { this.agendas = agendas; }
    public List<Cita> getCitas() { return citas; }
    public void setCitas(List<Cita> citas) { this.citas = citas; }
    public List<HistoriaClinica> getHistoriasClinicas() { return historiasClinicas; }
    public void setHistoriasClinicas(List<HistoriaClinica> historiasClinicas) { this.historiasClinicas = historiasClinicas; }
    public List<Alta> getAltas() { return altas; }
    public void setAltas(List<Alta> altas) { this.altas = altas; }

    public List<CartaDerivacion> getCartasDerivacion() { return cartasDerivacion; }
    public void setCartasDerivacion(List<CartaDerivacion> cartasDerivacion) { this.cartasDerivacion = cartasDerivacion; }

    public List<ConsentimientoInformado> getConsentimientos() { return consentimientos; }
    public void setConsentimientos(List<ConsentimientoInformado> consentimientos) { this.consentimientos = consentimientos; }

    public List<HojaEjercicios> getHojasEjercicios() { return hojasEjercicios; }
    public void setHojasEjercicios(List<HojaEjercicios> hojasEjercicios) { this.hojasEjercicios = hojasEjercicios; }
}
