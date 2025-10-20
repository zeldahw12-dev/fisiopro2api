package com.fisio.fisio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "nota_evolucion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaEvolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idnota_evolucion")
    private Integer idNotaEvolucion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agendaid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Agenda agenda;

    // Relación a ZonaCuerpo (para getZonaCuerpo/setZonaCuerpo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zonaid")
    private ZonaCuerpo zonaCuerpo;

    // ---- RANGOS/VALORES que tu service usa ----
    @Column(name = "flexion")
    private Integer flexion;

    @Column(name = "extension")
    private Integer extension;

    @Column(name = "abduccion")
    private Integer abduccion;

    @Column(name = "aduccion")
    private Integer aduccion;

    @Column(name = "rotacion_interna")
    private Integer rotacionInterna;

    @Column(name = "rotacion_externa")
    private Integer rotacionExterna;

    @Column(name = "pronacion")
    private Integer pronacion;

    @Column(name = "supinacion")
    private Integer supinacion;

    @Column(name = "flexion_palmar")
    private Integer flexionPalmar;

    @Column(name = "extension_dorsal")
    private Integer extensionDorsal;

    @Column(name = "desviacion_radial")
    private Integer desviacionRadial;

    @Column(name = "desviacion_cubital")
    private Integer desviacionCubital;

    @Column(name = "oposicion")
    private Integer oposicion;

    @Column(name = "flexion_plantar")
    private Integer flexionPlantar;

    @Column(name = "inversion")
    private Integer inversion;

    @Column(name = "eversion")
    private Integer eversion;

    @Column(name = "abduccion_aduccion")
    private Integer abduccionAduccion;

    @Column(name = "inclinacion_lateral")
    private Integer inclinacionLateral;

    @Column(name = "rotacion")
    private Integer rotacion;

    // Getters/Setters explícitos (si no confías en Lombok en runtime)
    public Integer getIdNotaEvolucion() { return idNotaEvolucion; }
    public void setIdNotaEvolucion(Integer idNotaEvolucion) { this.idNotaEvolucion = idNotaEvolucion; }
    public Agenda getAgenda() { return agenda; }
    public void setAgenda(Agenda agenda) { this.agenda = agenda; }
    public ZonaCuerpo getZonaCuerpo() { return zonaCuerpo; }
    public void setZonaCuerpo(ZonaCuerpo zonaCuerpo) { this.zonaCuerpo = zonaCuerpo; }
    public Integer getFlexion() { return flexion; }
    public void setFlexion(Integer flexion) { this.flexion = flexion; }
    public Integer getExtension() { return extension; }
    public void setExtension(Integer extension) { this.extension = extension; }
    public Integer getAbduccion() { return abduccion; }
    public void setAbduccion(Integer abduccion) { this.abduccion = abduccion; }
    public Integer getAduccion() { return aduccion; }
    public void setAduccion(Integer aduccion) { this.aduccion = aduccion; }
    public Integer getRotacionInterna() { return rotacionInterna; }
    public void setRotacionInterna(Integer rotacionInterna) { this.rotacionInterna = rotacionInterna; }
    public Integer getRotacionExterna() { return rotacionExterna; }
    public void setRotacionExterna(Integer rotacionExterna) { this.rotacionExterna = rotacionExterna; }
    public Integer getPronacion() { return pronacion; }
    public void setPronacion(Integer pronacion) { this.pronacion = pronacion; }
    public Integer getSupinacion() { return supinacion; }
    public void setSupinacion(Integer supinacion) { this.supinacion = supinacion; }
    public Integer getFlexionPalmar() { return flexionPalmar; }
    public void setFlexionPalmar(Integer flexionPalmar) { this.flexionPalmar = flexionPalmar; }
    public Integer getExtensionDorsal() { return extensionDorsal; }
    public void setExtensionDorsal(Integer extensionDorsal) { this.extensionDorsal = extensionDorsal; }
    public Integer getDesviacionRadial() { return desviacionRadial; }
    public void setDesviacionRadial(Integer desviacionRadial) { this.desviacionRadial = desviacionRadial; }
    public Integer getDesviacionCubital() { return desviacionCubital; }
    public void setDesviacionCubital(Integer desviacionCubital) { this.desviacionCubital = desviacionCubital; }
    public Integer getOposicion() { return oposicion; }
    public void setOposicion(Integer oposicion) { this.oposicion = oposicion; }
    public Integer getFlexionPlantar() { return flexionPlantar; }
    public void setFlexionPlantar(Integer flexionPlantar) { this.flexionPlantar = flexionPlantar; }
    public Integer getInversion() { return inversion; }
    public void setInversion(Integer inversion) { this.inversion = inversion; }
    public Integer getEversion() { return eversion; }
    public void setEversion(Integer eversion) { this.eversion = eversion; }
    public Integer getAbduccionAduccion() { return abduccionAduccion; }
    public void setAbduccionAduccion(Integer abduccionAduccion) { this.abduccionAduccion = abduccionAduccion; }
    public Integer getInclinacionLateral() { return inclinacionLateral; }
    public void setInclinacionLateral(Integer inclinacionLateral) { this.inclinacionLateral = inclinacionLateral; }
    public Integer getRotacion() { return rotacion; }
    public void setRotacion(Integer rotacion) { this.rotacion = rotacion; }
}
