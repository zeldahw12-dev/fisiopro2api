package com.fisio.fisio.dto;

import com.fisio.fisio.model.enums.EstadoCivil;
import com.fisio.fisio.model.enums.Sexo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HistoriaClinicaDTO {

    private Integer idHistoria;
    private Integer idPaciente;

    // INFORMACIÓN BÁSICA DEL PACIENTE
    private LocalDate fecha;
    private String nombre;
    private Integer edad;
    private LocalDate fechaNacimiento;
    private Sexo sexo;
    private String ocupacion;
    private String escolaridad;
    private EstadoCivil estadoCivil;
    private String religion;
    private String diagnosticoMedico;
    private String medicoTratante;
    private Boolean factura;
    private String aseguradora;
    private String telefono;

    // ANTECEDENTES FAMILIARES
    private String antecedentesMaternos;
    private String antecedentesPaternos;

    // HÁBITOS
    private Integer horasSueno;
    private String alimentacion;
    private String hidratacion;
    private String ocupacionDetalle;
    private String actividadFisica;
    private String resideCon;
    private String mascotas;

    // ANTECEDENTES PATOLÓGICOS
    private String ginecoobstetricos;
    private String quirurgicos;
    private String cardiacos;
    private String neurologicos;
    private String pulmonares;
    private String oftalmologicos;
    private String cronicoDegenerativos;
    private String otrosAntecedentes;
    private String medicamentos;
    private String suplementos;
    private String toxicomanias;

    // PADECIMIENTO ACTUAL
    private String padecimientoActual;

    // SIGNOS VITALES Y EXPLORACIÓN FÍSICA
    private String presionArterial;
    private BigDecimal temperatura;
    private Integer frecuenciaRespiratoria;
    private Integer frecuenciaCardiaca;
    private String saturacionOxigeno;
    private BigDecimal peso;
    private BigDecimal estatura;
    private String dolor;
    private String arcosMovilidad;
    private String fuerzaMuscular;
    private String sensibilidad;
    private String maniobrasEspeciales;
    private String pruebasEspeciales;

    // DIAGNÓSTICO Y TRATAMIENTO
    private String diagnosticoFisioterapeutico;
    private String objetivosCortoPlazo;
    private String objetivosMedianoPlazo;
    private String objetivosLargoPlazo;
    private String pronostico;
    private String observaciones;

    // INFORMACIÓN DEL FISIOTERAPEUTA
    private String fisioterapeuta;
    private String cedula;
    private LocalTime hora;

    // CAMPOS DE AUDITORÍA
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // Constructores
    public HistoriaClinicaDTO() {}

    public HistoriaClinicaDTO(Integer idPaciente, String nombre, Integer edad,
                              LocalDate fechaNacimiento, LocalDate fecha, Sexo sexo) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.edad = edad;
        this.fechaNacimiento = fechaNacimiento;
        this.fecha = fecha;
        this.sexo = sexo;
        this.factura = false;
    }

    // Getters y Setters
    public Integer getIdHistoria() { return idHistoria; }
    public void setIdHistoria(Integer idHistoria) { this.idHistoria = idHistoria; }

    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }

    public String getEscolaridad() { return escolaridad; }
    public void setEscolaridad(String escolaridad) { this.escolaridad = escolaridad; }

    public EstadoCivil getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(EstadoCivil estadoCivil) { this.estadoCivil = estadoCivil; }

    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }

    public String getDiagnosticoMedico() { return diagnosticoMedico; }
    public void setDiagnosticoMedico(String diagnosticoMedico) { this.diagnosticoMedico = diagnosticoMedico; }

    public String getMedicoTratante() { return medicoTratante; }
    public void setMedicoTratante(String medicoTratante) { this.medicoTratante = medicoTratante; }

    public Boolean getFactura() { return factura; }
    public void setFactura(Boolean factura) { this.factura = factura; }

    public String getAseguradora() { return aseguradora; }
    public void setAseguradora(String aseguradora) { this.aseguradora = aseguradora; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getAntecedentesMaternos() { return antecedentesMaternos; }
    public void setAntecedentesMaternos(String antecedentesMaternos) { this.antecedentesMaternos = antecedentesMaternos; }

    public String getAntecedentesPaternos() { return antecedentesPaternos; }
    public void setAntecedentesPaternos(String antecedentesPaternos) { this.antecedentesPaternos = antecedentesPaternos; }

    public Integer getHorasSueno() { return horasSueno; }
    public void setHorasSueno(Integer horasSueno) { this.horasSueno = horasSueno; }

    public String getAlimentacion() { return alimentacion; }
    public void setAlimentacion(String alimentacion) { this.alimentacion = alimentacion; }

    public String getHidratacion() { return hidratacion; }
    public void setHidratacion(String hidratacion) { this.hidratacion = hidratacion; }

    public String getOcupacionDetalle() { return ocupacionDetalle; }
    public void setOcupacionDetalle(String ocupacionDetalle) { this.ocupacionDetalle = ocupacionDetalle; }

    public String getActividadFisica() { return actividadFisica; }
    public void setActividadFisica(String actividadFisica) { this.actividadFisica = actividadFisica; }

    public String getResideCon() { return resideCon; }
    public void setResideCon(String resideCon) { this.resideCon = resideCon; }

    public String getMascotas() { return mascotas; }
    public void setMascotas(String mascotas) { this.mascotas = mascotas; }

    public String getGinecoobstetricos() { return ginecoobstetricos; }
    public void setGinecoobstetricos(String ginecoobstetricos) { this.ginecoobstetricos = ginecoobstetricos; }

    public String getQuirurgicos() { return quirurgicos; }
    public void setQuirurgicos(String quirurgicos) { this.quirurgicos = quirurgicos; }

    public String getCardiacos() { return cardiacos; }
    public void setCardiacos(String cardiacos) { this.cardiacos = cardiacos; }

    public String getNeurologicos() { return neurologicos; }
    public void setNeurologicos(String neurologicos) { this.neurologicos = neurologicos; }

    public String getPulmonares() { return pulmonares; }
    public void setPulmonares(String pulmonares) { this.pulmonares = pulmonares; }

    public String getOftalmologicos() { return oftalmologicos; }
    public void setOftalmologicos(String oftalmologicos) { this.oftalmologicos = oftalmologicos; }

    public String getCronicoDegenerativos() { return cronicoDegenerativos; }
    public void setCronicoDegenerativos(String cronicoDegenerativos) { this.cronicoDegenerativos = cronicoDegenerativos; }

    public String getOtrosAntecedentes() { return otrosAntecedentes; }
    public void setOtrosAntecedentes(String otrosAntecedentes) { this.otrosAntecedentes = otrosAntecedentes; }

    public String getMedicamentos() { return medicamentos; }
    public void setMedicamentos(String medicamentos) { this.medicamentos = medicamentos; }

    public String getSuplementos() { return suplementos; }
    public void setSuplementos(String suplementos) { this.suplementos = suplementos; }

    public String getToxicomanias() { return toxicomanias; }
    public void setToxicomanias(String toxicomanias) { this.toxicomanias = toxicomanias; }

    public String getPadecimientoActual() { return padecimientoActual; }
    public void setPadecimientoActual(String padecimientoActual) { this.padecimientoActual = padecimientoActual; }

    public String getPresionArterial() { return presionArterial; }
    public void setPresionArterial(String presionArterial) { this.presionArterial = presionArterial; }

    public BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }

    public Integer getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
    public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) { this.frecuenciaRespiratoria = frecuenciaRespiratoria; }

    public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }

    public String getSaturacionOxigeno() { return saturacionOxigeno; }
    public void setSaturacionOxigeno(String saturacionOxigeno) { this.saturacionOxigeno = saturacionOxigeno; }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }

    public BigDecimal getEstatura() { return estatura; }
    public void setEstatura(BigDecimal estatura) { this.estatura = estatura; }

    public String getDolor() { return dolor; }
    public void setDolor(String dolor) { this.dolor = dolor; }

    public String getArcosMovilidad() { return arcosMovilidad; }
    public void setArcosMovilidad(String arcosMovilidad) { this.arcosMovilidad = arcosMovilidad; }

    public String getFuerzaMuscular() { return fuerzaMuscular; }
    public void setFuerzaMuscular(String fuerzaMuscular) { this.fuerzaMuscular = fuerzaMuscular; }

    public String getSensibilidad() { return sensibilidad; }
    public void setSensibilidad(String sensibilidad) { this.sensibilidad = sensibilidad; }

    public String getManiobrasEspeciales() { return maniobrasEspeciales; }
    public void setManiobrasEspeciales(String maniobrasEspeciales) { this.maniobrasEspeciales = maniobrasEspeciales; }

    public String getPruebasEspeciales() { return pruebasEspeciales; }
    public void setPruebasEspeciales(String pruebasEspeciales) { this.pruebasEspeciales = pruebasEspeciales; }

    public String getDiagnosticoFisioterapeutico() { return diagnosticoFisioterapeutico; }
    public void setDiagnosticoFisioterapeutico(String diagnosticoFisioterapeutico) { this.diagnosticoFisioterapeutico = diagnosticoFisioterapeutico; }

    public String getObjetivosCortoPlazo() { return objetivosCortoPlazo; }
    public void setObjetivosCortoPlazo(String objetivosCortoPlazo) { this.objetivosCortoPlazo = objetivosCortoPlazo; }

    public String getObjetivosMedianoPlazo() { return objetivosMedianoPlazo; }
    public void setObjetivosMedianoPlazo(String objetivosMedianoPlazo) { this.objetivosMedianoPlazo = objetivosMedianoPlazo; }

    public String getObjetivosLargoPlazo() { return objetivosLargoPlazo; }
    public void setObjetivosLargoPlazo(String objetivosLargoPlazo) { this.objetivosLargoPlazo = objetivosLargoPlazo; }

    public String getPronostico() { return pronostico; }
    public void setPronostico(String pronostico) { this.pronostico = pronostico; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getFisioterapeuta() { return fisioterapeuta; }
    public void setFisioterapeuta(String fisioterapeuta) { this.fisioterapeuta = fisioterapeuta; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
