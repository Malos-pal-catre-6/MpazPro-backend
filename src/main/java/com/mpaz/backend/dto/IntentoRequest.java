package com.mpaz.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record IntentoRequest(
        @NotNull Long contenidoId,
        @Min(0) int correctas,
        @Min(1) int total
) {}
