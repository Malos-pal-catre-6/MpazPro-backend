package com.mpaz.backend.security;

// Representa al usuario ya autenticado por el JwtAuthFilter.
// Se obtiene en los controllers con: (UsuarioAutenticado) authentication.getPrincipal()
public record UsuarioAutenticado(Long id, String usuario, String rol) {
}
