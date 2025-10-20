package com.fisio.fisio.service;

import com.fisio.fisio.dto.ZonaCuerpoDTO;
import com.fisio.fisio.model.ZonaCuerpo;
import com.fisio.fisio.repository.ZonaCuerpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ZonaCuerpoService implements CommandLineRunner {

    @Autowired
    private ZonaCuerpoRepository zonaCuerpoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear zonas del cuerpo por defecto si no existen
        createDefaultZonasCuerpo();
    }

    private void createDefaultZonasCuerpo() {
        String[] zonasDefault = {
                // Miembro superior
                "Hombro (glenohumeral)",
                "Codo",
                "Muñeca",
                "Metacarpofalángicas (MCF)",
                "Interfalángicas proximales (IFP)",
                "Interfalángicas distales (IFD)",
                "Pulgar - Metacarpofalángica",
                "Pulgar - Interfalángica",

                // Miembro inferior
                "Pulgar - Trapeciometacarpiana (CMC)",
                "Cadera",
                "Rodilla",
                "Tobillo",
                "Metatarsofalángicas (MTF)",
                "Interfalángicas proximales (IFP) pie",
                "Interfalángicas distales (IFD) pie",

                // Columna
                "Columna cervical",
                "Columna lumbar"
        };

        for (String nombreZona : zonasDefault) {
            Optional<ZonaCuerpo> existingZona = zonaCuerpoRepository.findByNombre(nombreZona);
            if (existingZona.isEmpty()) {
                ZonaCuerpo zona = new ZonaCuerpo();
                zona.setNombre(nombreZona);
                zona.setFoto(null); // Se puede agregar URLs de imágenes después
                zonaCuerpoRepository.save(zona);
                System.out.println("Zona del cuerpo creada: " + nombreZona);
            }
        }
    }


    public List<ZonaCuerpoDTO> findAll() {
        return zonaCuerpoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ZonaCuerpoDTO> findById(Integer id) {
        return zonaCuerpoRepository.findById(id)
                .map(this::convertToDTO);
    }

    public ZonaCuerpoDTO save(ZonaCuerpoDTO zonaCuerpoDTO) {
        ZonaCuerpo zonaCuerpo = convertToEntity(zonaCuerpoDTO);
        ZonaCuerpo savedZonaCuerpo = zonaCuerpoRepository.save(zonaCuerpo);
        return convertToDTO(savedZonaCuerpo);
    }

    public Optional<ZonaCuerpoDTO> update(Integer id, ZonaCuerpoDTO zonaCuerpoDTO) {
        return zonaCuerpoRepository.findById(id)
                .map(existingZonaCuerpo -> {
                    existingZonaCuerpo.setNombre(zonaCuerpoDTO.getNombre());
                    existingZonaCuerpo.setFoto(zonaCuerpoDTO.getFoto());
                    return convertToDTO(zonaCuerpoRepository.save(existingZonaCuerpo));
                });
    }

    public boolean deleteById(Integer id) {
        if (zonaCuerpoRepository.existsById(id)) {
            zonaCuerpoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<ZonaCuerpoDTO> findByNombre(String nombre) {
        return zonaCuerpoRepository.findByNombre(nombre)
                .map(this::convertToDTO);
    }

    public List<ZonaCuerpoDTO> findByNombreContaining(String nombre) {
        return zonaCuerpoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ZonaCuerpoDTO convertToDTO(ZonaCuerpo zonaCuerpo) {
        ZonaCuerpoDTO dto = new ZonaCuerpoDTO();
        dto.setIdZonaCuerpo(zonaCuerpo.getIdZonaCuerpo());
        dto.setNombre(zonaCuerpo.getNombre());
        dto.setFoto(zonaCuerpo.getFoto());
        return dto;
    }

    private ZonaCuerpo convertToEntity(ZonaCuerpoDTO zonaCuerpoDTO) {
        ZonaCuerpo zonaCuerpo = new ZonaCuerpo();
        zonaCuerpo.setIdZonaCuerpo(zonaCuerpoDTO.getIdZonaCuerpo());
        zonaCuerpo.setNombre(zonaCuerpoDTO.getNombre());
        zonaCuerpo.setFoto(zonaCuerpoDTO.getFoto());
        return zonaCuerpo;
    }
}
