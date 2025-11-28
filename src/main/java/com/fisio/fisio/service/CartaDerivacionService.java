// src/main/java/com/fisio/fisio/service/CartaDerivacionService.java
package com.fisio.fisio.service;

import com.fisio.fisio.dto.CartaDerivacionDTO;
import com.fisio.fisio.model.CartaDerivacion;
import com.fisio.fisio.model.Paciente;
import com.fisio.fisio.model.Prioridad;
import com.fisio.fisio.repository.CartaDerivacionRepository;
import com.fisio.fisio.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartaDerivacionService {

    private final CartaDerivacionRepository cartaRepo;
    private final PacienteRepository pacienteRepo;

    public CartaDerivacionService(CartaDerivacionRepository cartaRepo, PacienteRepository pacienteRepo) {
        this.cartaRepo = cartaRepo;
        this.pacienteRepo = pacienteRepo;
    }

    /* ========================== API ========================== */

    /** Obtener una carta por idCarta (Long) */
    @Transactional(readOnly = true)
    public CartaDerivacionDTO obtenerPorId(Long idCarta) {
        CartaDerivacion ent = cartaRepo.findById(idCarta)
                .orElseThrow(() -> new IllegalArgumentException("Carta no encontrada: " + idCarta));
        return toDTO(ent);
    }

    /** Listar cartas por idPaciente (Integer) */
    @Transactional(readOnly = true)
    public List<CartaDerivacionDTO> obtenerPorPaciente(Integer idPaciente) {
        Paciente p = pacienteRepo.findById(idPaciente)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + idPaciente));
        List<CartaDerivacion> list = cartaRepo.findByPaciente(p);
        List<CartaDerivacionDTO> out = new ArrayList<>();
        for (CartaDerivacion e : list) out.add(toDTO(e));
        return out;
    }

    /** Crear nueva carta */
    public CartaDerivacionDTO crear(CartaDerivacionDTO dto) {
        Paciente p = pacienteRepo.findById(dto.getIdPaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + dto.getIdPaciente()));
        CartaDerivacion e = new CartaDerivacion();
        fillEntityFromDto(e, dto, p);
        CartaDerivacion saved = cartaRepo.save(e);
        return toDTO(saved);
    }

    /** Actualizar carta existente */
    public CartaDerivacionDTO actualizar(Long idCarta, CartaDerivacionDTO dto) {
        CartaDerivacion e = cartaRepo.findById(idCarta)
                .orElseThrow(() -> new IllegalArgumentException("Carta no encontrada: " + idCarta));

        // Si cambia el paciente, recupéralo; si no, conserva el actual
        Paciente p = Optional.ofNullable(dto.getIdPaciente())
                .map(pid -> pacienteRepo.findById(pid).orElseThrow(() ->
                        new IllegalArgumentException("Paciente no encontrado: " + pid)))
                .orElse(e.getPaciente());

        fillEntityFromDto(e, dto, p);
        CartaDerivacion saved = cartaRepo.save(e);
        return toDTO(saved);
    }

    /** Eliminar carta */
    public void eliminar(Long idCarta) {
        if (!cartaRepo.existsById(idCarta)) {
            throw new IllegalArgumentException("Carta no encontrada: " + idCarta);
        }
        cartaRepo.deleteById(idCarta);
    }

    /* ===================== MAPEOS DTO <-> ENT ===================== */

    private CartaDerivacionDTO toDTO(CartaDerivacion e) {
        CartaDerivacionDTO d = new CartaDerivacionDTO();

        // idCarta en DTO es Integer; la entidad usa Long -> convertir con cuidado
        if (e.getIdCarta() != null) {
            long val = e.getIdCarta();
            d.setIdCarta(val > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) val);
        }

        d.setIdPaciente(e.getIdPaciente());
        d.setFecha(e.getFecha());
        d.setHora(e.getHora());

        d.setRemitente(e.getRemitente());
        d.setCedula(e.getCedula());

        d.setDestinatarioNombre(e.getDestinatarioNombre());
        d.setDestinatarioEspecialidad(e.getDestinatarioEspecialidad());
        d.setClinicaDestino(e.getClinicaDestino());

        d.setMotivoDerivacion(e.getMotivoDerivacion());
        d.setResumenCaso(e.getResumenCaso());
        d.setHallazgosRelevantes(e.getHallazgosRelevantes());
        d.setPruebasRealizadas(e.getPruebasRealizadas());
        d.setTratamientosIntentados(e.getTratamientosIntentados());
        d.setRespuestaTratamiento(e.getRespuestaTratamiento());
        d.setRiesgosBanderasRojas(e.getRiesgosBanderasRojas());
        d.setMedicamentosActuales(e.getMedicamentosActuales());
        d.setAlergias(e.getAlergias());
        d.setRecomendaciones(e.getRecomendaciones());
        d.setObjetivoDerivacion(e.getObjetivoDerivacion());

        d.setPrioridad(e.getPrioridad() != null ? e.getPrioridad().name() : "MEDIA");

        d.setContactoRemitente(e.getContactoRemitente());
        d.setContactoPaciente(e.getContactoPaciente());


        d.setObservaciones(e.getObservaciones());

        return d;
    }

    private void fillEntityFromDto(CartaDerivacion e, CartaDerivacionDTO d, Paciente paciente) {
        e.setPaciente(paciente);

        // ⚠️ NADA de isBlank() sobre LocalDate ni parse(): el DTO ya trae LocalDate
        LocalDate fecha = d.getFecha() != null ? d.getFecha() : LocalDate.now();
        e.setFecha(fecha);
        e.setHora(d.getHora());

        e.setRemitente(d.getRemitente());
        e.setCedula(d.getCedula());

        e.setDestinatarioNombre(d.getDestinatarioNombre());
        e.setDestinatarioEspecialidad(d.getDestinatarioEspecialidad());
        e.setClinicaDestino(d.getClinicaDestino());

        e.setMotivoDerivacion(d.getMotivoDerivacion());
        e.setResumenCaso(d.getResumenCaso());
        e.setHallazgosRelevantes(d.getHallazgosRelevantes());
        e.setPruebasRealizadas(d.getPruebasRealizadas());
        e.setTratamientosIntentados(d.getTratamientosIntentados());
        e.setRespuestaTratamiento(d.getRespuestaTratamiento());
        e.setRiesgosBanderasRojas(d.getRiesgosBanderasRojas());
        e.setMedicamentosActuales(d.getMedicamentosActuales());
        e.setAlergias(d.getAlergias());
        e.setRecomendaciones(d.getRecomendaciones());
        e.setObjetivoDerivacion(d.getObjetivoDerivacion());

        // Prioridad: String -> Enum (segura)
        e.setPrioridad(parsePrioridadSafe(d.getPrioridad()));

        e.setContactoRemitente(d.getContactoRemitente());
        e.setContactoPaciente(d.getContactoPaciente());

        e.setObservaciones(d.getObservaciones());
    }

    private Prioridad parsePrioridadSafe(String p) {
        if (p == null || p.trim().isEmpty()) return Prioridad.MEDIA;
        try {
            return Prioridad.valueOf(p.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return Prioridad.MEDIA;
        }
    }
}
