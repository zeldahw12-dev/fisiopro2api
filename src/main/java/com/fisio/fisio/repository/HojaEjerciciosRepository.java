package com.fisio.fisio.repository;

import com.fisio.fisio.model.HojaEjercicios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HojaEjerciciosRepository extends JpaRepository<HojaEjercicios, Integer> {

    List<HojaEjercicios> findByPaciente_IdPaciente(Integer pacienteId);

    List<HojaEjercicios> findByTituloContainingIgnoreCase(String titulo);
}
