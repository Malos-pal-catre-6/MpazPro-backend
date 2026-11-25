package com.mpaz.backend.controller;

import com.mpaz.backend.dto.*;
import com.mpaz.backend.security.UsuarioAutenticado;
import com.mpaz.backend.service.ContenidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contenidos")
@RequiredArgsConstructor
public class ContenidoController {

    private final ContenidoService contenidoService;

    private UsuarioAutenticado actual(Authentication auth) {
        return (UsuarioAutenticado) auth.getPrincipal();
    }

    // Docente crea contenido (nace en EN_REVISION, a la espera de UTP)
    @PostMapping
    public ResponseEntity<ContenidoResponse> crear(Authentication auth, @Valid @RequestBody ContenidoRequest req) {
        return ResponseEntity.ok(contenidoService.crear(actual(auth).id(), req));
    }

    // Docente consulta su propio contenido (cualquier estado)
    @GetMapping("/mios")
    public ResponseEntity<List<ContenidoResponse>> mios(Authentication auth) {
        return ResponseEntity.ok(contenidoService.misContenidos(actual(auth).id()));
    }

    // UTP ve lo pendiente de revision
    @GetMapping("/pendientes")
    public ResponseEntity<List<ContenidoResponse>> pendientes() {
        return ResponseEntity.ok(contenidoService.pendientes());
    }

    // Estudiante ve solo lo autorizado
    @GetMapping("/autorizados")
    public ResponseEntity<List<ContenidoResponse>> autorizados() {
        return ResponseEntity.ok(contenidoService.autorizados());
    }

    // UTP autoriza o rechaza
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ContenidoResponse> cambiarEstado(@PathVariable Long id,
                                                            @Valid @RequestBody EstadoUpdateRequest req) {
        return ResponseEntity.ok(contenidoService.cambiarEstado(id, req.nuevoEstado()));
    }

    // Docente define el maximo de intentos de SU contenido
    @PatchMapping("/{id}/max-intentos")
    public ResponseEntity<ContenidoResponse> definirMaxIntentos(Authentication auth, @PathVariable Long id,
                                                                 @Valid @RequestBody MaxIntentosRequest req) {
        return ResponseEntity.ok(contenidoService.definirMaxIntentos(id, actual(auth).id(), req.maxIntentos()));
    }
}
