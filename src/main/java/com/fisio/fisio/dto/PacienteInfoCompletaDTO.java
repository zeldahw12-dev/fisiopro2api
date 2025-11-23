package com.fisio.fisio.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PacienteInfoCompletaDTO {

    private String nombrePaciente;
    private String seguro; // aseguradora
    private String diagnosticoMedico;
    private String cedulaFisioterapeuta;
    private String nombreFisioterapeuta;
    private List<AgendaInfoDTO> agendas;

    // Constructor vacío
    public PacienteInfoCompletaDTO() {}

    // Constructor con parámetros
    public PacienteInfoCompletaDTO(String nombrePaciente, String seguro, String diagnosticoMedico,
                                   String cedulaFisioterapeuta, String nombreFisioterapeuta,
                                   List<AgendaInfoDTO> agendas) {
        this.nombrePaciente = nombrePaciente;
        this.seguro = seguro;
        this.diagnosticoMedico = diagnosticoMedico;
        this.cedulaFisioterapeuta = cedulaFisioterapeuta;
        this.nombreFisioterapeuta = nombreFisioterapeuta;
        this.agendas = agendas;
    }

    // Getters y Setters
    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getSeguro() { return seguro; }
    public void setSeguro(String seguro) { this.seguro = seguro; }

    public String getDiagnosticoMedico() { return diagnosticoMedico; }
    public void setDiagnosticoMedico(String diagnosticoMedico) { this.diagnosticoMedico = diagnosticoMedico; }

    public String getCedulaFisioterapeuta() { return cedulaFisioterapeuta; }
    public void setCedulaFisioterapeuta(String cedulaFisioterapeuta) { this.cedulaFisioterapeuta = cedulaFisioterapeuta; }

    public String getNombreFisioterapeuta() { return nombreFisioterapeuta; }
    public void setNombreFisioterapeuta(String nombreFisioterapeuta) { this.nombreFisioterapeuta = nombreFisioterapeuta; }

    public List<AgendaInfoDTO> getAgendas() { return agendas; }
    public void setAgendas(List<AgendaInfoDTO> agendas) { this.agendas = agendas; }

    // Clase interna para la información de agenda
    public static class AgendaInfoDTO {
        private LocalDateTime fecha;
        private String intervencion;

        public AgendaInfoDTO() {}

        public AgendaInfoDTO(LocalDateTime fecha, String intervencion) {
            this.fecha = fecha;
            this.intervencion = intervencion;
        }

        public LocalDateTime getFecha() { return fecha; }
        public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

        public String getIntervencion() { return intervencion; }
        public void setIntervencion(String intervencion) { this.intervencion = intervencion; }
    }
}
