// src/main/java/com/fisio/fisio/service/ConsentimientoService.java
package com.fisio.fisio.service;

import com.fisio.fisio.dto.ConsentimientoDTO;
import com.fisio.fisio.model.ConsentimientoInformado;
import com.fisio.fisio.model.Paciente;
import com.fisio.fisio.repository.ConsentimientoRepository;
import com.fisio.fisio.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ConsentimientoService {

    private final ConsentimientoRepository repo;
    private final PacienteRepository pacienteRepo;

    public ConsentimientoService(ConsentimientoRepository repo, PacienteRepository pacienteRepo) {
        this.repo = repo; this.pacienteRepo = pacienteRepo;
    }

    @Transactional(readOnly = true)
    public List<ConsentimientoDTO> getByPacienteId(Integer idPaciente) {
        Paciente p = pacienteRepo.findById(idPaciente)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + idPaciente));
        List<ConsentimientoInformado> list = repo.findByPaciente(p);
        List<ConsentimientoDTO> out = new ArrayList<>();
        for (ConsentimientoInformado e : list) out.add(toDTO(e));
        return out;
    }

    @Transactional(readOnly = true)
    public ConsentimientoDTO getById(Long id) {
        ConsentimientoInformado e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consentimiento no encontrado: " + id));
        return toDTO(e);
    }

    public ConsentimientoDTO create(ConsentimientoDTO d) {
        Paciente p = pacienteRepo.findById(d.getIdPaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + d.getIdPaciente()));
        ConsentimientoInformado e = new ConsentimientoInformado();
        fill(e, d, p);
        return toDTO(repo.save(e));
    }

    public ConsentimientoDTO update(Long id, ConsentimientoDTO d) {
        ConsentimientoInformado e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consentimiento no encontrado: " + id));
        Paciente p = d.getIdPaciente() != null
                ? pacienteRepo.findById(d.getIdPaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + d.getIdPaciente()))
                : e.getPaciente();
        fill(e, d, p);
        return toDTO(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Consentimiento no encontrado: " + id);
        repo.deleteById(id);
    }

    /* ----- mapping ----- */
    private ConsentimientoDTO toDTO(ConsentimientoInformado e) {
        ConsentimientoDTO d = new ConsentimientoDTO();
        if (e.getIdConsentimiento() != null) {
            long v = e.getIdConsentimiento();
            d.setIdConsentimiento(v > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) v);
        }
        d.setIdPaciente(e.getIdPaciente());
        d.setFecha(e.getFecha());
        d.setHora(e.getHora());
        d.setProcedimiento(e.getProcedimiento());
        d.setRiesgos(e.getRiesgos());
        d.setBeneficios(e.getBeneficios());
        d.setAlternativas(e.getAlternativas());
        d.setResponsabilidadesPaciente(e.getResponsabilidadesPaciente());
        d.setResponsabilidadesProfesional(e.getResponsabilidadesProfesional());
        d.setNotasAdicionales(e.getNotasAdicionales());
        d.setPreguntasResueltas(e.getPreguntasResueltas());
        d.setTestigoNombre(e.getTestigoNombre());
        d.setTestigoContacto(e.getTestigoContacto());
        return d;
    }

    private void fill(ConsentimientoInformado e, ConsentimientoDTO d, Paciente p) {
        e.setPaciente(p);
        e.setFecha(d.getFecha() != null ? d.getFecha() : LocalDate.now());
        e.setHora(d.getHora());

        e.setProcedimiento(d.getProcedimiento());
        e.setRiesgos(d.getRiesgos());
        e.setBeneficios(d.getBeneficios());
        e.setAlternativas(d.getAlternativas());
        e.setResponsabilidadesPaciente(d.getResponsabilidadesPaciente());
        e.setResponsabilidadesProfesional(d.getResponsabilidadesProfesional());
        e.setNotasAdicionales(d.getNotasAdicionales());
        e.setPreguntasResueltas(d.getPreguntasResueltas() != null ? d.getPreguntasResueltas() : Boolean.TRUE);
        e.setTestigoNombre(d.getTestigoNombre());
        e.setTestigoContacto(d.getTestigoContacto());
    }
}
