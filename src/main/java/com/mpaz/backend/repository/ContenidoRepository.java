package com.mpaz.backend.repository;

import com.mpaz.backend.model.Contenido;
import com.mpaz.backend.model.EstadoContenido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContenidoRepository extends JpaRepository<Contenido, Long> {
    List<Contenido> findByEstado(EstadoContenido estado);
    List<Contenido> findByDocenteId(Long docenteId);
}
