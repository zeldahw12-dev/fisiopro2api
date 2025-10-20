package com.fisio.fisio.service;

import com.fisio.fisio.dto.HistoriaClinicaDTO;
import com.fisio.fisio.model.HistoriaClinica;
import com.fisio.fisio.model.Paciente;
import com.fisio.fisio.repository.HistoriaClinicaRepository;
import com.fisio.fisio.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class HistoriaClinicaService {

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<HistoriaClinicaDTO> obtenerTodasLasHistorias() {
        return historiaClinicaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<HistoriaClinicaDTO> obtenerHistoriaPorId(Integer id) {
        return historiaClinicaRepository.findById(id)
                .map(this::convertirADTO);
    }

    public List<HistoriaClinicaDTO> obtenerHistoriasPorPaciente(Integer idPaciente) {
        return historiaClinicaRepository.findByPaciente_IdPaciente(idPaciente)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<HistoriaClinicaDTO> obtenerHistoriasPorPacienteOrdenadas(Integer idPaciente) {
        return historiaClinicaRepository.findByPacienteOrderByFechaDesc(idPaciente)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<HistoriaClinicaDTO> buscarPorNombre(String nombre) {
        return historiaClinicaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<HistoriaClinicaDTO> buscarPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return historiaClinicaRepository.findByFechaBetween(fechaInicio, fechaFin)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<HistoriaClinicaDTO> buscarPorFisioterapeuta(String fisioterapeuta) {
        return historiaClinicaRepository.findByFisioterapeutaContainingIgnoreCase(fisioterapeuta)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<HistoriaClinicaDTO> buscarPorMedicoTratante(String medicoTratante) {
        return historiaClinicaRepository.findByMedicoTratanteContainingIgnoreCase(medicoTratante)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<HistoriaClinicaDTO> buscarPorDiagnosticoMedico(String diagnostico) {
        return historiaClinicaRepository.findByDiagnosticoMedicoContaining(diagnostico)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<HistoriaClinicaDTO> buscarPorDiagnosticoFisioterapeutico(String diagnostico) {
        return historiaClinicaRepository.findByDiagnosticoFisioterapeuticoContaining(diagnostico)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<HistoriaClinicaDTO> buscarPorEscolaridad(String escolaridad) {
        return historiaClinicaRepository.findByEscolaridadContainingIgnoreCase(escolaridad)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<HistoriaClinicaDTO> buscarPorPadecimientoActual(String padecimiento) {
        return historiaClinicaRepository.findByPadecimientoActualContaining(padecimiento)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<HistoriaClinicaDTO> buscarPorPruebasEspeciales(String pruebas) {
        return historiaClinicaRepository.findByPruebasEspecialesContaining(pruebas)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Long contarHistoriasPorPaciente(Integer idPaciente) {
        return historiaClinicaRepository.countByPaciente(idPaciente);
    }

    public HistoriaClinicaDTO crearHistoria(HistoriaClinicaDTO historiaDTO) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(historiaDTO.getIdPaciente());
        if (pacienteOpt.isEmpty()) {
            throw new RuntimeException("Paciente no encontrado con ID: " + historiaDTO.getIdPaciente());
        }

        HistoriaClinica historia = convertirAEntidad(historiaDTO);
        historia.setPaciente(pacienteOpt.get());
        historia.setIdHistoria(null);
        historia.setFechaCreacion(LocalDateTime.now());

        // Si el DTO trae null, guardamos false; si trae true, true; si trae false, false.
        historia.setFactura(Boolean.TRUE.equals(historiaDTO.getFactura()));

        HistoriaClinica historiaGuardada = historiaClinicaRepository.save(historia);
        return convertirADTO(historiaGuardada);
    }

    public HistoriaClinicaDTO actualizarHistoria(Integer id, HistoriaClinicaDTO historiaDTO) {
        Optional<HistoriaClinica> historiaExistente = historiaClinicaRepository.findById(id);
        if (historiaExistente.isEmpty()) {
            throw new RuntimeException("Historia clínica no encontrada con ID: " + id);
        }

        HistoriaClinica historia = historiaExistente.get();
        actualizarCamposEntidad(historia, historiaDTO);
        historia.setFechaActualizacion(LocalDateTime.now());

        HistoriaClinica historiaActualizada = historiaClinicaRepository.save(historia);
        return convertirADTO(historiaActualizada);
    }

    public void eliminarHistoria(Integer id) {
        if (!historiaClinicaRepository.existsById(id)) {
            throw new RuntimeException("Historia clínica no encontrada con ID: " + id);
        }
        historiaClinicaRepository.deleteById(id);
    }

    private void actualizarCamposEntidad(HistoriaClinica historia, HistoriaClinicaDTO dto) {
        // Información básica
        historia.setFecha(dto.getFecha());
        historia.setNombre(dto.getNombre());
        historia.setEdad(dto.getEdad());
        historia.setFechaNacimiento(dto.getFechaNacimiento());
        historia.setSexo(dto.getSexo());
        historia.setOcupacion(dto.getOcupacion());
        historia.setEscolaridad(dto.getEscolaridad());
        historia.setEstadoCivil(dto.getEstadoCivil());
        historia.setReligion(dto.getReligion());
        historia.setDiagnosticoMedico(dto.getDiagnosticoMedico());
        historia.setMedicoTratante(dto.getMedicoTratante());
        historia.setFactura(Boolean.TRUE.equals(dto.getFactura()));
        historia.setAseguradora(dto.getAseguradora());
        historia.setTelefono(dto.getTelefono());

        // Antecedentes familiares
        historia.setAntecedentesMaternos(dto.getAntecedentesMaternos());
        historia.setAntecedentesPaternos(dto.getAntecedentesPaternos());

        // Hábitos
        historia.setHorasSueno(dto.getHorasSueno());
        historia.setAlimentacion(dto.getAlimentacion());
        historia.setHidratacion(dto.getHidratacion());
        historia.setOcupacionDetalle(dto.getOcupacionDetalle());
        historia.setActividadFisica(dto.getActividadFisica());
        historia.setResideCon(dto.getResideCon());
        historia.setMascotas(dto.getMascotas());

        // Antecedentes patológicos
        historia.setGinecoobstetricos(dto.getGinecoobstetricos());
        historia.setQuirurgicos(dto.getQuirurgicos());
        historia.setCardiacos(dto.getCardiacos());
        historia.setNeurologicos(dto.getNeurologicos());
        historia.setPulmonares(dto.getPulmonares());
        historia.setOftalmologicos(dto.getOftalmologicos());
        historia.setCronicoDegenerativos(dto.getCronicoDegenerativos());
        historia.setOtrosAntecedentes(dto.getOtrosAntecedentes());
        historia.setMedicamentos(dto.getMedicamentos());
        historia.setSuplementos(dto.getSuplementos());
        historia.setToxicomanias(dto.getToxicomanias());

        // Padecimiento actual
        historia.setPadecimientoActual(dto.getPadecimientoActual());

        // Signos vitales y exploración física
        historia.setPresionArterial(dto.getPresionArterial());
        historia.setTemperatura(dto.getTemperatura());
        historia.setFrecuenciaRespiratoria(dto.getFrecuenciaRespiratoria());
        historia.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        historia.setSaturacionOxigeno(dto.getSaturacionOxigeno());
        historia.setPeso(dto.getPeso());
        historia.setEstatura(dto.getEstatura());
        historia.setDolor(dto.getDolor());
        historia.setArcosMovilidad(dto.getArcosMovilidad());
        historia.setFuerzaMuscular(dto.getFuerzaMuscular());
        historia.setSensibilidad(dto.getSensibilidad());
        historia.setManiobrasEspeciales(dto.getManiobrasEspeciales());
        historia.setPruebasEspeciales(dto.getPruebasEspeciales());

        // Diagnóstico y tratamiento
        historia.setDiagnosticoFisioterapeutico(dto.getDiagnosticoFisioterapeutico());
        historia.setObjetivosCortoPlazo(dto.getObjetivosCortoPlazo());
        historia.setObjetivosMedianoPlazo(dto.getObjetivosMedianoPlazo());
        historia.setObjetivosLargoPlazo(dto.getObjetivosLargoPlazo());
        historia.setPronostico(dto.getPronostico());
        historia.setObservaciones(dto.getObservaciones());

        // Información del fisioterapeuta
        historia.setFisioterapeuta(dto.getFisioterapeuta());
        historia.setCedula(dto.getCedula());
        historia.setHora(dto.getHora());
    }

    private HistoriaClinicaDTO convertirADTO(HistoriaClinica historia) {
        HistoriaClinicaDTO dto = new HistoriaClinicaDTO();

        dto.setIdHistoria(historia.getIdHistoria());
        dto.setIdPaciente(historia.getPaciente().getIdPaciente());

        // Información básica
        dto.setFecha(historia.getFecha());
        dto.setNombre(historia.getNombre());
        dto.setEdad(historia.getEdad());
        dto.setFechaNacimiento(historia.getFechaNacimiento());
        dto.setSexo(historia.getSexo());
        dto.setOcupacion(historia.getOcupacion());
        dto.setEscolaridad(historia.getEscolaridad());
        dto.setEstadoCivil(historia.getEstadoCivil());
        dto.setReligion(historia.getReligion());
        dto.setDiagnosticoMedico(historia.getDiagnosticoMedico());
        dto.setMedicoTratante(historia.getMedicoTratante());
        dto.setFactura(historia.getFactura());
        dto.setAseguradora(historia.getAseguradora());
        dto.setTelefono(historia.getTelefono());

        // Antecedentes familiares
        dto.setAntecedentesMaternos(historia.getAntecedentesMaternos());
        dto.setAntecedentesPaternos(historia.getAntecedentesPaternos());

        // Hábitos
        dto.setHorasSueno(historia.getHorasSueno());
        dto.setAlimentacion(historia.getAlimentacion());
        dto.setHidratacion(historia.getHidratacion());
        dto.setOcupacionDetalle(historia.getOcupacionDetalle());
        dto.setActividadFisica(historia.getActividadFisica());
        dto.setResideCon(historia.getResideCon());
        dto.setMascotas(historia.getMascotas());

        // Antecedentes patológicos
        dto.setGinecoobstetricos(historia.getGinecoobstetricos());
        dto.setQuirurgicos(historia.getQuirurgicos());
        dto.setCardiacos(historia.getCardiacos());
        dto.setNeurologicos(historia.getNeurologicos());
        dto.setPulmonares(historia.getPulmonares());
        dto.setOftalmologicos(historia.getOftalmologicos());
        dto.setCronicoDegenerativos(historia.getCronicoDegenerativos());
        dto.setOtrosAntecedentes(historia.getOtrosAntecedentes());
        dto.setMedicamentos(historia.getMedicamentos());
        dto.setSuplementos(historia.getSuplementos());
        dto.setToxicomanias(historia.getToxicomanias());

        // Padecimiento actual
        dto.setPadecimientoActual(historia.getPadecimientoActual());

        // Signos vitales y exploración física
        dto.setPresionArterial(historia.getPresionArterial());
        dto.setTemperatura(historia.getTemperatura());
        dto.setFrecuenciaRespiratoria(historia.getFrecuenciaRespiratoria());
        dto.setFrecuenciaCardiaca(historia.getFrecuenciaCardiaca());
        dto.setSaturacionOxigeno(historia.getSaturacionOxigeno());
        dto.setPeso(historia.getPeso());
        dto.setEstatura(historia.getEstatura());
        dto.setDolor(historia.getDolor());
        dto.setArcosMovilidad(historia.getArcosMovilidad());
        dto.setFuerzaMuscular(historia.getFuerzaMuscular());
        dto.setSensibilidad(historia.getSensibilidad());
        dto.setManiobrasEspeciales(historia.getManiobrasEspeciales());
        dto.setPruebasEspeciales(historia.getPruebasEspeciales());

        // Diagnóstico y tratamiento
        dto.setDiagnosticoFisioterapeutico(historia.getDiagnosticoFisioterapeutico());
        dto.setObjetivosCortoPlazo(historia.getObjetivosCortoPlazo());
        dto.setObjetivosMedianoPlazo(historia.getObjetivosMedianoPlazo());
        dto.setObjetivosLargoPlazo(historia.getObjetivosLargoPlazo());
        dto.setPronostico(historia.getPronostico());
        dto.setObservaciones(historia.getObservaciones());

        // Información del fisioterapeuta
        dto.setFisioterapeuta(historia.getFisioterapeuta());
        dto.setCedula(historia.getCedula());
        dto.setHora(historia.getHora());

        // Auditoría
        dto.setFechaCreacion(historia.getFechaCreacion());
        dto.setFechaActualizacion(historia.getFechaActualizacion());

        return dto;
    }

    private HistoriaClinica convertirAEntidad(HistoriaClinicaDTO dto) {
        HistoriaClinica historia = new HistoriaClinica();

        // Información básica
        historia.setFecha(dto.getFecha());
        historia.setNombre(dto.getNombre());
        historia.setEdad(dto.getEdad());
        historia.setFechaNacimiento(dto.getFechaNacimiento());
        historia.setSexo(dto.getSexo());
        historia.setOcupacion(dto.getOcupacion());
        historia.setEscolaridad(dto.getEscolaridad());
        historia.setEstadoCivil(dto.getEstadoCivil());
        historia.setReligion(dto.getReligion());
        historia.setDiagnosticoMedico(dto.getDiagnosticoMedico());
        historia.setMedicoTratante(dto.getMedicoTratante());
        historia.setFactura(Boolean.TRUE.equals(dto.getFactura()));
        historia.setAseguradora(dto.getAseguradora());
        historia.setTelefono(dto.getTelefono());

        // Antecedentes familiares
        historia.setAntecedentesMaternos(dto.getAntecedentesMaternos());
        historia.setAntecedentesPaternos(dto.getAntecedentesPaternos());

        // Hábitos
        historia.setHorasSueno(dto.getHorasSueno());
        historia.setAlimentacion(dto.getAlimentacion());
        historia.setHidratacion(dto.getHidratacion());
        historia.setOcupacionDetalle(dto.getOcupacionDetalle());
        historia.setActividadFisica(dto.getActividadFisica());
        historia.setResideCon(dto.getResideCon());
        historia.setMascotas(dto.getMascotas());

        // Antecedentes patológicos
        historia.setGinecoobstetricos(dto.getGinecoobstetricos());
        historia.setQuirurgicos(dto.getQuirurgicos());
        historia.setCardiacos(dto.getCardiacos());
        historia.setNeurologicos(dto.getNeurologicos());
        historia.setPulmonares(dto.getPulmonares());
        historia.setOftalmologicos(dto.getOftalmologicos());
        historia.setCronicoDegenerativos(dto.getCronicoDegenerativos());
        historia.setOtrosAntecedentes(dto.getOtrosAntecedentes());
        historia.setMedicamentos(dto.getMedicamentos());
        historia.setSuplementos(dto.getSuplementos());
        historia.setToxicomanias(dto.getToxicomanias());

        // Padecimiento actual
        historia.setPadecimientoActual(dto.getPadecimientoActual());

        // Signos vitales y exploración física
        historia.setPresionArterial(dto.getPresionArterial());
        historia.setTemperatura(dto.getTemperatura());
        historia.setFrecuenciaRespiratoria(dto.getFrecuenciaRespiratoria());
        historia.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        historia.setSaturacionOxigeno(dto.getSaturacionOxigeno());
        historia.setPeso(dto.getPeso());
        historia.setEstatura(dto.getEstatura());
        historia.setDolor(dto.getDolor());
        historia.setArcosMovilidad(dto.getArcosMovilidad());
        historia.setFuerzaMuscular(dto.getFuerzaMuscular());
        historia.setSensibilidad(dto.getSensibilidad());
        historia.setManiobrasEspeciales(dto.getManiobrasEspeciales());
        historia.setPruebasEspeciales(dto.getPruebasEspeciales());

        // Diagnóstico y tratamiento
        historia.setDiagnosticoFisioterapeutico(dto.getDiagnosticoFisioterapeutico());
        historia.setObjetivosCortoPlazo(dto.getObjetivosCortoPlazo());
        historia.setObjetivosMedianoPlazo(dto.getObjetivosMedianoPlazo());
        historia.setObjetivosLargoPlazo(dto.getObjetivosLargoPlazo());
        historia.setPronostico(dto.getPronostico());
        historia.setObservaciones(dto.getObservaciones());

        // Información del fisioterapeuta
        historia.setFisioterapeuta(dto.getFisioterapeuta());
        historia.setCedula(dto.getCedula());
        historia.setHora(dto.getHora());

        return historia;
    }
}
