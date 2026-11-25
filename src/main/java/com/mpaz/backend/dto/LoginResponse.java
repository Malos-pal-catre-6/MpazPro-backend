package com.mpaz.backend.dto;

public record LoginResponse(
        String token,
        Long id,
        String nombre,
        String usuario,
        String rol
) {}
