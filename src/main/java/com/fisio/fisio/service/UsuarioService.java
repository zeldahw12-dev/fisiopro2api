package com.fisio.fisio.service;

import com.fisio.fisio.dto.UsuarioCreateDTO;
import com.fisio.fisio.dto.UsuarioDTO;
import com.fisio.fisio.model.PlanTipo;
import com.fisio.fisio.model.Usuario;
import com.fisio.fisio.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    /* ====== CRUD READ ====== */

    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> findById(Integer id) {
        return usuarioRepository.findById(id).map(this::toDTO);
    }

    public Optional<UsuarioDTO> findByEmail(String email) {
        return usuarioRepository.findByEmail(email).map(this::toDTO);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existsByNickname(String nickname) {
        return usuarioRepository.existsByNickname(nickname);
    }

    /* ====== CREATE ====== */

    @Transactional
    public UsuarioDTO save(UsuarioCreateDTO dto) {
        Usuario entity = new Usuario();
        entity.setNickname(dto.getNickname());
        entity.setNombre(dto.getNombre());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setEmail(dto.getEmail());

        // ✅ Hasheamos contraseña al crear usuario
        entity.setContra(passwordEncoder.encode(dto.getContra()));

        entity.setFoto(dto.getFoto());
        entity.setProfesion(dto.getProfesion());

        // 👉 NUEVO: siempre FREE al crear
        entity.setPlan(PlanTipo.FREE);

        // tokenVersion por defecto (0) si no lo seteas aquí
        Usuario saved = usuarioRepository.save(entity);
        return toDTO(saved);
    }


    /* ====== UPDATE PARCIAL (usando UsuarioDTO; no pisa con null) ====== */

    @Transactional
    public Optional<UsuarioDTO> update(Integer id, UsuarioDTO dto) {
        return usuarioRepository.findById(id).map(u -> {

            if (dto.getNickname() != null) u.setNickname(dto.getNickname());
            if (dto.getNombre() != null) u.setNombre(dto.getNombre());
            if (dto.getFechaNacimiento() != null) u.setFechaNacimiento(dto.getFechaNacimiento());
            if (dto.getEmail() != null) u.setEmail(dto.getEmail());
            if (dto.getFoto() != null) u.setFoto(dto.getFoto());
            if (dto.getProfesion() != null) u.setProfesion(dto.getProfesion());

            // Contraseña: solo si viene NO nula y no vacía
            if (dto.getContra() != null && !dto.getContra().isBlank()) {
                // ✅ Consideramos que lo que llega es la nueva contraseña en claro
                u.setContra(passwordEncoder.encode(dto.getContra()));
            }

            Usuario saved = usuarioRepository.save(u);
            return toDTO(saved);
        });
    }

    /* ====== UPDATE SOLO CONTRASEÑA ====== */

    @Transactional
    public Optional<UsuarioDTO> updatePassword(Integer id, String nuevaContra) {
        return usuarioRepository.findById(id).map(u -> {
            if (nuevaContra == null || nuevaContra.isBlank()) return toDTO(u);
            // ✅ guardamos hash
            u.setContra(passwordEncoder.encode(nuevaContra));
            Usuario saved = usuarioRepository.save(u);
            return toDTO(saved);
        });
    }

    /* ====== DELETE ====== */

    @Transactional
    public boolean deleteById(Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /* ====== MAPPERS ====== */

    private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNickname(usuario.getNickname());
        dto.setNombre(usuario.getNombre());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setEmail(usuario.getEmail());
        dto.setContra(usuario.getContra());
        dto.setFoto(usuario.getFoto());
        dto.setProfesion(usuario.getProfesion());

        // 👉 NUEVO: mandamos el plan como texto ("FREE" / "PREMIUM")
        if (usuario.getPlan() != null) {
            dto.setPlan(usuario.getPlan().name());
        } else {
            dto.setPlan("FREE");
        }

        return dto;
    }

    @Transactional
    public Optional<UsuarioDTO> updatePlan(Integer id, String nuevoPlan) {
        return usuarioRepository.findById(id).map(u -> {
            PlanTipo planTipo;
            try {
                planTipo = PlanTipo.valueOf(nuevoPlan.toUpperCase());
            } catch (IllegalArgumentException ex) {
                // aquí podrías lanzar una excepción custom para 400 Bad Request
                throw new IllegalArgumentException("Plan inválido. Usa FREE o PREMIUM.");
            }
            u.setPlan(planTipo);
            Usuario saved = usuarioRepository.save(u);
            return toDTO(saved);
        });
    }

    /* =============================================================================
     *                           DASHBOARD (SOLO LECTURA)
     * =============================================================================
     * GET /usuarios/{id}/dashboard -> Map<String, Object> con métricas útiles
     * ============================================================================= */

    public Optional<Map<String, Object>> buildDashboard(Integer idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) return Optional.empty();

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDateTime startOfWeek = today.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime last30 = now.minusDays(30);
        LocalDateTime last14 = now.minusDays(14);

        Map<String, Object> dto = new LinkedHashMap<>();

        Long totalPacientes = (Long) em.createQuery("""
            select count(p) from Paciente p
            where p.usuario.idUsuario = :id
        """)
                .setParameter("id", idUsuario)
                .getSingleResult();

        Long sesionesTotales = (Long) em.createQuery("""
            select count(a) from Agenda a
            where a.paciente.usuario.idUsuario = :id
        """)
                .setParameter("id", idUsuario)
                .getSingleResult();

        Long sesionesSemana = (Long) em.createQuery("""
            select count(a) from Agenda a
            where a.paciente.usuario.idUsuario = :id
              and a.fecha between :from and :to
        """)
                .setParameter("id", idUsuario)
                .setParameter("from", startOfWeek)
                .setParameter("to", now)
                .getSingleResult();

        Long sesionesMes = (Long) em.createQuery("""
            select count(a) from Agenda a
            where a.paciente.usuario.idUsuario = :id
              and a.fecha between :from and :to
        """)
                .setParameter("id", idUsuario)
                .setParameter("from", startOfMonth)
                .setParameter("to", now)
                .getSingleResult();

        Long pacientesActivos30 = (Long) em.createQuery("""
            select count(distinct a.paciente.idPaciente)
            from Agenda a
            where a.paciente.usuario.idUsuario = :id
              and a.fecha >= :from
        """)
                .setParameter("id", idUsuario)
                .setParameter("from", last30)
                .getSingleResult();

        List<Object[]> proximasCitasRows = em.createQuery("""
            select c.idCita, c.fecha, c.paciente.idPaciente, c.paciente.nombre
            from Cita c
            where c.paciente.usuario.idUsuario = :id
              and c.fecha >= :now
            order by c.fecha asc
        """, Object[].class)
                .setParameter("id", idUsuario)
                .setParameter("now", now)
                .setMaxResults(5)
                .getResultList();

        Integer citasProximas = proximasCitasRows.size();
        LocalDateTime proximaCita = proximasCitasRows.isEmpty() ? null : (LocalDateTime) proximasCitasRows.get(0)[1];

        Double promedioSesionesPorPaciente = (totalPacientes == 0) ? 0.0 : (double) sesionesTotales / totalPacientes;

        Long historiasConFactura = (Long) em.createQuery("""
            select count(h) from HistoriaClinica h
            where h.paciente.usuario.idUsuario = :id and h.factura = true
        """)
                .setParameter("id", idUsuario)
                .getSingleResult();

        Long historiasSinFactura = (Long) em.createQuery("""
            select count(h) from HistoriaClinica h
            where h.paciente.usuario.idUsuario = :id and (h.factura = false or h.factura is null)
        """)
                .setParameter("id", idUsuario)
                .getSingleResult();

        List<Object[]> zonasRaw = em.createQuery("""
            select ne.zonaCuerpo.idZonaCuerpo,
                   ne.zonaCuerpo.nombre,
                   count(ne) as totalSesiones,
                   count(distinct ne.agenda.paciente.idPaciente) as pacientesUnicos
            from NotaEvolucion ne
            where ne.agenda.paciente.usuario.idUsuario = :id
              and ne.zonaCuerpo.idZonaCuerpo is not null
            group by ne.zonaCuerpo.idZonaCuerpo, ne.zonaCuerpo.nombre
            order by totalSesiones desc
        """, Object[].class)
                .setParameter("id", idUsuario)
                .setMaxResults(5)
                .getResultList();

        List<Map<String, Object>> topZonas = zonasRaw.stream().map(r -> {
            Map<String, Object> z = new LinkedHashMap<>();
            z.put("idZona", r[0]);
            z.put("nombreZona", r[1]);
            z.put("totalSesiones", ((Long) r[2]));
            z.put("pacientesUnicos", ((Long) r[3]));
            return z;
        }).collect(Collectors.toList());

        List<Object[]> mejorasRaw = em.createQuery("""
            select ne.zonaCuerpo.idZonaCuerpo, ne.zonaCuerpo.nombre,
                   min(ne.flexion), max(ne.flexion)
            from NotaEvolucion ne
            where ne.agenda.paciente.usuario.idUsuario = :id
              and ne.zonaCuerpo.idZonaCuerpo is not null
              and ne.flexion is not null
            group by ne.zonaCuerpo.idZonaCuerpo, ne.zonaCuerpo.nombre
        """, Object[].class)
                .setParameter("id", idUsuario)
                .getResultList();

        List<Map<String, Object>> mejorasPorZona = mejorasRaw.stream().map(r -> {
            Integer idZona = (Integer) r[0];
            String nombre = (String) r[1];
            Integer minFlex = (Integer) r[2];
            Integer maxFlex = (Integer) r[3];
            Double delta = (minFlex == null || maxFlex == null) ? null : (double) (maxFlex - minFlex);
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("idZona", idZona);
            m.put("nombreZona", nombre);
            m.put("deltaFlexionProm", delta);
            return m;
        }).collect(Collectors.toList());

        List<Object[]> serie = em.createQuery("""
            select date(a.fecha), count(a)
            from Agenda a
            where a.paciente.usuario.idUsuario = :id
              and a.fecha between :from and :to
            group by date(a.fecha)
            order by date(a.fecha)
        """, Object[].class)
                .setParameter("id", idUsuario)
                .setParameter("from", last14)
                .setParameter("to", now)
                .getResultList();

        Map<LocalDate, Long> sesionesPorDia = new LinkedHashMap<>();
        for (int i = 0; i <= 14; i++) {
            sesionesPorDia.put(today.minusDays(14 - i), 0L);
        }
        for (Object[] row : serie) {
            LocalDate d = (row[0] instanceof java.sql.Date)
                    ? ((java.sql.Date) row[0]).toLocalDate()
                    : (LocalDate) row[0];
            Long c = (Long) row[1];
            sesionesPorDia.put(d, c);
        }

        // Usamos fechaNacimiento para calcular rangos de edad
        List<Object[]> fechasNacimientoRaw = em.createQuery("""
            select p.fechaNacimiento
            from Paciente p
            where p.usuario.idUsuario = :id and p.fechaNacimiento is not null
        """, Object[].class)
                .setParameter("id", idUsuario)
                .getResultList();

        long r0_17 = 0, r18_30 = 0, r31_45 = 0, r46_60 = 0, r61 = 0;
        for (Object[] row : fechasNacimientoRaw) {
            LocalDate fechaNacimiento = (LocalDate) row[0];
            if (fechaNacimiento == null) continue;
            int edad = Period.between(fechaNacimiento, today).getYears();
            if (edad <= 17) r0_17++;
            else if (edad <= 30) r18_30++;
            else if (edad <= 45) r31_45++;
            else if (edad <= 60) r46_60++;
            else r61++;
        }
        Map<String, Long> pacientesPorRangoEdad = new LinkedHashMap<>();
        pacientesPorRangoEdad.put("0-17", r0_17);
        pacientesPorRangoEdad.put("18-30", r18_30);
        pacientesPorRangoEdad.put("31-45", r31_45);
        pacientesPorRangoEdad.put("46-60", r46_60);
        pacientesPorRangoEdad.put("61+", r61);

        List<Object[]> ultimasAgendas = em.createQuery("""
            select a.idAgenda, a.fecha, a.paciente.idPaciente, a.paciente.nombre, a.intervencion
            from Agenda a
            where a.paciente.usuario.idUsuario = :id
            order by a.fecha desc
        """, Object[].class)
                .setParameter("id", idUsuario)
                .setMaxResults(10)
                .getResultList();

        List<Map<String, Object>> actividadReciente = ultimasAgendas.stream().map(r -> {
            Map<String, Object> a = new LinkedHashMap<>();
            a.put("idAgenda", r[0]);
            a.put("fechaAgenda", r[1]);
            a.put("idPaciente", r[2]);
            a.put("nombrePaciente", r[3]);
            a.put("intervencion", r[4]);
            return a;
        }).collect(Collectors.toList());

        List<Map<String, Object>> proximasCitas = proximasCitasToMap(proximasCitasRows);

        Double promedioDiasEntreSesiones = calcularPromedioDiasEntreSesiones(idUsuario);

        dto.put("totalPacientes", totalPacientes.intValue());
        dto.put("pacientesActivosUltimos30d", pacientesActivos30.intValue());
        dto.put("sesionesEstaSemana", sesionesSemana.intValue());
        dto.put("sesionesEsteMes", sesionesMes.intValue());
        dto.put("sesionesTotales", sesionesTotales);
        dto.put("promedioSesionesPorPaciente", round(promedioSesionesPorPaciente, 2));
        dto.put("citasProximas", citasProximas);
        dto.put("proximaCita", proximaCita);

        dto.put("promedioDiasEntreSesiones", promedioDiasEntreSesiones);
        dto.put("historiasConFactura", historiasConFactura);
        dto.put("historiasSinFactura", historiasSinFactura);

        dto.put("topZonas", topZonas);
        dto.put("mejorasPorZona", mejorasPorZona);

        dto.put("sesionesPorDiaUltimos14", sesionesPorDia);
        dto.put("pacientesPorRangoEdad", pacientesPorRangoEdad);

        dto.put("actividadReciente", actividadReciente);
        dto.put("proximasCitas", proximasCitas);

        return Optional.of(dto);
    }

    private List<Map<String, Object>> proximasCitasToMap(List<Object[]> rows) {
        return rows.stream().map(r -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("idCita", r[0]);
            m.put("fecha", r[1]);
            m.put("idPaciente", r[2]);
            m.put("nombrePaciente", r[3]);
            return m;
        }).collect(Collectors.toList());
    }

    /** Promedio global de días entre sesiones por paciente (simple). */
    private Double calcularPromedioDiasEntreSesiones(Integer idUsuario) {
        List<Object[]> agendas = em.createQuery("""
            select a.paciente.idPaciente, a.fecha
            from Agenda a
            where a.paciente.usuario.idUsuario = :id
            order by a.paciente.idPaciente asc, a.fecha asc
        """)
                .setParameter("id", idUsuario)
                .setMaxResults(5000)
                .getResultList();

        if (agendas.isEmpty()) return 0.0;

        Integer prevPaciente = null;
        LocalDateTime prevFecha = null;
        long difDiasAcumulado = 0;
        long pares = 0;

        for (Object[] row : agendas) {
            Integer pac = (Integer) row[0];
            LocalDateTime f = (LocalDateTime) row[1];

            if (prevPaciente != null && pac.equals(prevPaciente) && prevFecha != null) {
                long dias = Duration.between(prevFecha, f).toDays();
                if (dias >= 0) {
                    difDiasAcumulado += dias;
                    pares++;
                }
            }
            prevPaciente = pac;
            prevFecha = f;
        }

        if (pares == 0) return 0.0;
        return round((double) difDiasAcumulado / pares, 2);
    }

    private Double round(Double v, int scale) {
        if (v == null) return null;
        double p = Math.pow(10, scale);
        return Math.round(v * p) / p;
    }
}
