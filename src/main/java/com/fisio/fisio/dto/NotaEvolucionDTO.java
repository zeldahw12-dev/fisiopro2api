package com.fisio.fisio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaEvolucionDTO {
    private Integer idNotaEvolucion;

    private Integer flexion;
    private Integer extension;
    private Integer abduccion;
    private Integer aduccion;
    private Integer rotacionInterna;
    private Integer rotacionExterna;
    private Integer pronacion;
    private Integer supinacion;
    private Integer flexionPalmar;
    private Integer extensionDorsal;
    private Integer desviacionRadial;
    private Integer desviacionCubital;
    private Integer oposicion;
    private Integer flexionPlantar;
    private Integer inversion;
    private Integer eversion;
    private Integer abduccionAduccion;
    private Integer inclinacionLateral;
    private Integer rotacion;

    private Integer agendaId;
    private Integer zonaCuerpoId;
    private String nombreZonaCuerpo;

    public Integer getIdNotaEvolucion() {
        return idNotaEvolucion;
    }

    public void setIdNotaEvolucion(Integer idNotaEvolucion) {
        this.idNotaEvolucion = idNotaEvolucion;
    }

    public Integer getFlexion() {
        return flexion;
    }

    public void setFlexion(Integer flexion) {
        this.flexion = flexion;
    }

    public Integer getExtension() {
        return extension;
    }

    public void setExtension(Integer extension) {
        this.extension = extension;
    }

    public Integer getAbduccion() {
        return abduccion;
    }

    public void setAbduccion(Integer abduccion) {
        this.abduccion = abduccion;
    }

    public Integer getAduccion() {
        return aduccion;
    }

    public void setAduccion(Integer aduccion) {
        this.aduccion = aduccion;
    }

    public Integer getRotacionInterna() {
        return rotacionInterna;
    }

    public void setRotacionInterna(Integer rotacionInterna) {
        this.rotacionInterna = rotacionInterna;
    }

    public Integer getRotacionExterna() {
        return rotacionExterna;
    }

    public void setRotacionExterna(Integer rotacionExterna) {
        this.rotacionExterna = rotacionExterna;
    }

    public Integer getPronacion() {
        return pronacion;
    }

    public void setPronacion(Integer pronacion) {
        this.pronacion = pronacion;
    }

    public Integer getSupinacion() {
        return supinacion;
    }

    public void setSupinacion(Integer supinacion) {
        this.supinacion = supinacion;
    }

    public Integer getFlexionPalmar() {
        return flexionPalmar;
    }

    public void setFlexionPalmar(Integer flexionPalmar) {
        this.flexionPalmar = flexionPalmar;
    }

    public Integer getExtensionDorsal() {
        return extensionDorsal;
    }

    public void setExtensionDorsal(Integer extensionDorsal) {
        this.extensionDorsal = extensionDorsal;
    }

    public Integer getDesviacionRadial() {
        return desviacionRadial;
    }

    public void setDesviacionRadial(Integer desviacionRadial) {
        this.desviacionRadial = desviacionRadial;
    }

    public Integer getDesviacionCubital() {
        return desviacionCubital;
    }

    public void setDesviacionCubital(Integer desviacionCubital) {
        this.desviacionCubital = desviacionCubital;
    }

    public Integer getOposicion() {
        return oposicion;
    }

    public void setOposicion(Integer oposicion) {
        this.oposicion = oposicion;
    }

    public Integer getFlexionPlantar() {
        return flexionPlantar;
    }

    public void setFlexionPlantar(Integer flexionPlantar) {
        this.flexionPlantar = flexionPlantar;
    }

    public Integer getInversion() {
        return inversion;
    }

    public void setInversion(Integer inversion) {
        this.inversion = inversion;
    }

    public Integer getEversion() {
        return eversion;
    }

    public void setEversion(Integer eversion) {
        this.eversion = eversion;
    }

    public Integer getAbduccionAduccion() {
        return abduccionAduccion;
    }

    public void setAbduccionAduccion(Integer abduccionAduccion) {
        this.abduccionAduccion = abduccionAduccion;
    }

    public Integer getInclinacionLateral() {
        return inclinacionLateral;
    }

    public void setInclinacionLateral(Integer inclinacionLateral) {
        this.inclinacionLateral = inclinacionLateral;
    }

    public Integer getRotacion() {
        return rotacion;
    }

    public void setRotacion(Integer rotacion) {
        this.rotacion = rotacion;
    }

    public Integer getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(Integer agendaId) {
        this.agendaId = agendaId;
    }

    public Integer getZonaCuerpoId() {
        return zonaCuerpoId;
    }

    public void setZonaCuerpoId(Integer zonaCuerpoId) {
        this.zonaCuerpoId = zonaCuerpoId;
    }

    public String getNombreZonaCuerpo() {
        return nombreZonaCuerpo;
    }

    public void setNombreZonaCuerpo(String nombreZonaCuerpo) {
        this.nombreZonaCuerpo = nombreZonaCuerpo;
    }
}
