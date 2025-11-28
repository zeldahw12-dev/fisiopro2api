package com.fisio.fisio.model;

import com.fisio.fisio.model.enums.EstadoCivil;
import com.fisio.fisio.model.enums.Sexo;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "historias_clinicas")
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historia")
    private Integer idHistoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Paciente paciente;

    // ——— Metadatos usados por AgendaService y tu PDF ———
    @Column(name = "aseguradora", length = 120)
    private String aseguradora;

    @Column(name = "diagnostico_medico", length = 500)
    private String diagnosticoMedico;

    @Column(name = "fisioterapeuta", length = 120)
    private String fisioterapeuta;

    @Column(name = "cedula", length = 50)
    private String cedula;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // ——— Encabezado de historia ———
    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "nombre", length = 120)
    private String nombre;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", length = 10)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil", length = 20)
    private EstadoCivil estadoCivil;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "religion", length = 80)
    private String religion;

    @Column(name = "medico_tratante", length = 120)
    private String medicoTratante;

    // ——— Datos sociales / estilo de vida ———
    @Column(name = "ocupacion", length = 120)
    private String ocupacion;

    @Column(name = "ocupacion_detalle", length = 255)
    private String ocupacionDetalle;

    @Column(name = "escolaridad", length = 120)
    private String escolaridad;

    @Column(name = "actividad_fisica", length = 255)
    private String actividadFisica;

    @Column(name = "reside_con", length = 255)
    private String resideCon;

    @Column(name = "mascotas", length = 255)
    private String mascotas;

    @Column(name = "alimentacion", length = 255)
    private String alimentacion;

    @Column(name = "hidratacion", length = 255)
    private String hidratacion;

    @Column(name = "horas_sueno")
    private Integer horasSueno;

    // ——— Antecedentes ———
    @Column(name = "antecedentes_maternos", length = 500)
    private String antecedentesMaternos;

    @Column(name = "antecedentes_paternos", length = 500)
    private String antecedentesPaternos;

    @Column(name = "ginecoobstetricos", length = 500)
    private String ginecoobstetricos;

    @Column(name = "quirurgicos", length = 500)
    private String quirurgicos;

    @Column(name = "cardiacos", length = 500)
    private String cardiacos;

    @Column(name = "neurologicos", length = 500)
    private String neurologicos;

    @Column(name = "pulmonares", length = 500)
    private String pulmonares;

    @Column(name = "oftalmologicos", length = 500)
    private String oftalmologicos;

    @Column(name = "cronico_degenerativos", length = 500)
    private String cronicoDegenerativos;

    @Column(name = "otros_antecedentes", length = 1000)
    private String otrosAntecedentes;

    @Column(name = "medicamentos", length = 1000)
    private String medicamentos;

    @Column(name = "suplementos", length = 1000)
    private String suplementos;

    @Column(name = "toxicomanias", length = 1000)
    private String toxicomanias;

    // ——— Padecimiento actual ———
    @Column(name = "padecimiento_actual", columnDefinition = "TEXT")
    private String padecimientoActual;

    // ——— Signos vitales / antropometría ———
    @Column(name = "presion_arterial", length = 30)
    private String presionArterial;

    @Column(name = "temperatura", precision = 5, scale = 2)
    private BigDecimal temperatura;

    @Column(name = "frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Column(name = "saturacion_oxigeno", length = 10)
    private String saturacionOxigeno;

    @Column(name = "peso", precision = 6, scale = 2)
    private BigDecimal peso;

    @Column(name = "estatura", precision = 5, scale = 2)
    private BigDecimal estatura;

    // ——— Exploración física ———
    @Column(name = "dolor", length = 255)
    private String dolor;

    @Column(name = "arcos_movilidad", columnDefinition = "TEXT")
    private String arcosMovilidad;

    @Column(name = "fuerza_muscular", columnDefinition = "TEXT")
    private String fuerzaMuscular;

    @Column(name = "sensibilidad", columnDefinition = "TEXT")
    private String sensibilidad;

    @Column(name = "maniobras_especiales", columnDefinition = "TEXT")
    private String maniobrasEspeciales;

    @Column(name = "pruebas_especiales", columnDefinition = "TEXT")
    private String pruebasEspeciales;

    // ——— Diagnóstico y planes ———
    @Column(name = "diagnostico_fisioterapeutico", columnDefinition = "TEXT")
    private String diagnosticoFisioterapeutico;

    @Column(name = "objetivos_corto_plazo", columnDefinition = "TEXT")
    private String objetivosCortoPlazo;

    @Column(name = "objetivos_mediano_plazo", columnDefinition = "TEXT")
    private String objetivosMedianoPlazo;

    @Column(name = "objetivos_largo_plazo", columnDefinition = "TEXT")
    private String objetivosLargoPlazo;

    @Column(name = "pronostico", columnDefinition = "TEXT")
    private String pronostico;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "factura")
    private boolean factura;

    // ——— Getters y Setters ———
    public Integer getIdHistoria() { return idHistoria; }
    public void setIdHistoria(Integer idHistoria) { this.idHistoria = idHistoria; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public String getAseguradora() { return aseguradora; }
    public void setAseguradora(String aseguradora) { this.aseguradora = aseguradora; }
    public String getDiagnosticoMedico() { return diagnosticoMedico; }
    public void setDiagnosticoMedico(String diagnosticoMedico) { this.diagnosticoMedico = diagnosticoMedico; }
    public String getFisioterapeuta() { return fisioterapeuta; }
    public void setFisioterapeuta(String fisioterapeuta) { this.fisioterapeuta = fisioterapeuta; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }
    public EstadoCivil getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(EstadoCivil estadoCivil) { this.estadoCivil = estadoCivil; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }
    public String getMedicoTratante() { return medicoTratante; }
    public void setMedicoTratante(String medicoTratante) { this.medicoTratante = medicoTratante; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
    public String getOcupacionDetalle() { return ocupacionDetalle; }
    public void setOcupacionDetalle(String ocupacionDetalle) { this.ocupacionDetalle = ocupacionDetalle; }
    public String getEscolaridad() { return escolaridad; }
    public void setEscolaridad(String escolaridad) { this.escolaridad = escolaridad; }
    public String getActividadFisica() { return actividadFisica; }
    public void setActividadFisica(String actividadFisica) { this.actividadFisica = actividadFisica; }
    public String getResideCon() { return resideCon; }
    public void setResideCon(String resideCon) { this.resideCon = resideCon; }
    public String getMascotas() { return mascotas; }
    public void setMascotas(String mascotas) { this.mascotas = mascotas; }
    public String getAlimentacion() { return alimentacion; }
    public void setAlimentacion(String alimentacion) { this.alimentacion = alimentacion; }
    public String getHidratacion() { return hidratacion; }
    public void setHidratacion(String hidratacion) { this.hidratacion = hidratacion; }
    public Integer getHorasSueno() { return horasSueno; }
    public void setHorasSueno(Integer horasSueno) { this.horasSueno = horasSueno; }

    public String getAntecedentesMaternos() { return antecedentesMaternos; }
    public void setAntecedentesMaternos(String antecedentesMaternos) { this.antecedentesMaternos = antecedentesMaternos; }
    public String getAntecedentesPaternos() { return antecedentesPaternos; }
    public void setAntecedentesPaternos(String antecedentesPaternos) { this.antecedentesPaternos = antecedentesPaternos; }
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

    public boolean isFactura() { return factura; }
    public boolean getFactura() { return factura; }
    public void setFactura(boolean factura) { this.factura = factura; }
}