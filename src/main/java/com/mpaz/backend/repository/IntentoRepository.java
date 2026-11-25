package com.mpaz.backend.repository;

import com.mpaz.backend.model.Intento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntentoRepository extends JpaRepository<Intento, Long> {
    List<Intento> findByEstudianteIdAndContenidoId(Long estudianteId, Long contenidoId);
    List<Intento> findByContenidoId(Long contenidoId);
    List<Intento> findAllByOrderByFechaDesc();
}
