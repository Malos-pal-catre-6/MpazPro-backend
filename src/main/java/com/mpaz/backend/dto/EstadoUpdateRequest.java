package com.mpaz.backend.dto;

import jakarta.validation.constraints.NotBlank;

// Usado por UTP para autorizar/rechazar: nuevoEstado = "AUTORIZADO" | "RECHAZADO" | "EN_REVISION"
public record EstadoUpdateRequest(@NotBlank String nuevoEstado) {}
