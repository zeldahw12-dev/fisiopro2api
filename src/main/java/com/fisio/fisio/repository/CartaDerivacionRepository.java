package com.fisio.fisio.repository;

import com.fisio.fisio.model.CartaDerivacion;
import com.fisio.fisio.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartaDerivacionRepository extends JpaRepository<CartaDerivacion, Long> {
    List<CartaDerivacion> findByPaciente(Paciente paciente);
}
