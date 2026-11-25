package com.mpaz.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

// Lo que envia el Docente al crear contenido
public record ContenidoRequest(
        @NotBlank String asignatura,
        @NotBlank String unidad,
        @NotBlank String objetivo,
        @NotBlank String explicacion,
        String ejemplo,
        @NotEmpty @Valid List<PreguntaDto> preguntas
) {}
