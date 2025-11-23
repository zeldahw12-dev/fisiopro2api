// src/main/java/com/fisio/fisio/repository/ConsentimientoRepository.java
package com.fisio.fisio.repository;

import com.fisio.fisio.model.ConsentimientoInformado;
import com.fisio.fisio.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsentimientoRepository extends JpaRepository<ConsentimientoInformado, Long> {
    List<ConsentimientoInformado> findByPaciente(Paciente paciente);
}
