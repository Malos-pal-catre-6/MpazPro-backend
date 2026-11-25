package com.mpaz.backend.dto;

import com.mpaz.backend.model.Contenido;

import java.time.LocalDateTime;
import java.util.List;

public record ContenidoResponse(
        Long id,
        String asignatura,
        String unidad,
        String objetivo,
        String explicacion,
        String ejemplo,
        String estado,
        Integer maxIntentos,
        String docenteNombre,
        LocalDateTime creadoEn,
        List<PreguntaDto> preguntas
) {
    public static ContenidoResponse desde(Contenido c) {
        return new ContenidoResponse(
                c.getId(),
                c.getAsignatura(),
                c.getUnidad(),
                c.getObjetivo(),
                c.getExplicacion(),
                c.getEjemplo(),
                c.getEstado().name(),
                c.getMaxIntentos(),
                c.getDocente().getNombre(),
                c.getCreadoEn(),
                c.getPreguntas().stream()
                        .map(p -> new PreguntaDto(p.getId(), p.getEnunciado(), p.getAlternativas(), p.getCorrecta()))
                        .toList()
        );
    }
}
