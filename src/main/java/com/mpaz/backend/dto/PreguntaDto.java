package com.mpaz.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PreguntaDto(
        Long id,
        @NotBlank String enunciado,
        @NotEmpty List<String> alternativas,
        @NotNull Integer correcta
) {}
