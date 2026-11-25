package com.mpaz.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotBlank String nombre,
        @NotBlank String usuario,
        @NotBlank @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres") String password,
        @NotBlank String rol // "ESTUDIANTE" | "DOCENTE" | "UTP"
) {}
