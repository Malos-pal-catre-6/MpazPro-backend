package com.mpaz.backend.controller;

import com.mpaz.backend.dto.IntentoRequest;
import com.mpaz.backend.dto.IntentoResponse;
import com.mpaz.backend.security.UsuarioAutenticado;
import com.mpaz.backend.service.IntentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/intentos")
@RequiredArgsConstructor
public class IntentoController {

    private final IntentoService intentoService;

    private UsuarioAutenticado actual(Authentication auth) {
        return (UsuarioAutenticado) auth.getPrincipal();
    }

    // Estudiante registra el resultado de una actividad/prueba
    @PostMapping
    public ResponseEntity<IntentoResponse> registrar(Authentication auth, @Valid @RequestBody IntentoRequest req) {
        return ResponseEntity.ok(intentoService.registrar(actual(auth).id(), req));
    }

    // Estudiante consulta cuantos intentos lleva en un contenido
    @GetMapping("/mios/{contenidoId}")
    public ResponseEntity<List<IntentoResponse>> misIntentos(Authentication auth, @PathVariable Long contenidoId) {
        return ResponseEntity.ok(intentoService.misIntentos(actual(auth).id(), contenidoId));
    }

    // Docente consulta los intentos de un contenido suyo
    @GetMapping("/contenido/{contenidoId}")
    public ResponseEntity<List<IntentoResponse>> porContenido(@PathVariable Long contenidoId) {
        return ResponseEntity.ok(intentoService.porContenido(contenidoId));
    }
}
