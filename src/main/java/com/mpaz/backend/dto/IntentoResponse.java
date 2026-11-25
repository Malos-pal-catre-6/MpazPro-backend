package com.mpaz.backend.dto;

import com.mpaz.backend.model.Intento;

import java.time.LocalDateTime;

public record IntentoResponse(
        Long id,
        String estudianteNombre,
        Long contenidoId,
        String asignatura,
        Integer correctas,
        Integer total,
        Integer numeroIntento,
        LocalDateTime fecha
) {
    public static IntentoResponse desde(Intento i) {
        return new IntentoResponse(
                i.getId(),
                i.getEstudiante().getNombre(),
                i.getContenido().getId(),
                i.getContenido().getAsignatura(),
                i.getCorrectas(),
                i.getTotal(),
                i.getNumeroIntento(),
                i.getFecha()
        );
    }
}
