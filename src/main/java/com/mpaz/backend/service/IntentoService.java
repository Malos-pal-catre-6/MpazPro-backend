package com.mpaz.backend.service;

import com.mpaz.backend.dto.IntentoRequest;
import com.mpaz.backend.dto.IntentoResponse;
import com.mpaz.backend.model.Contenido;
import com.mpaz.backend.model.EstadoContenido;
import com.mpaz.backend.model.Intento;
import com.mpaz.backend.model.Usuario;
import com.mpaz.backend.repository.IntentoRepository;
import com.mpaz.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IntentoService {

    private final IntentoRepository intentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ContenidoService contenidoService;

    @Transactional
    public IntentoResponse registrar(Long estudianteId, IntentoRequest req) {
        Contenido contenido = contenidoService.obtener(req.contenidoId());

        if (contenido.getEstado() != EstadoContenido.AUTORIZADO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El contenido aun no esta autorizado");
        }

        Usuario estudiante = usuarioRepository.findById(estudianteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado"));

        List<Intento> previos = intentoRepository.findByEstudianteIdAndContenidoId(estudianteId, contenido.getId());

        if (previos.size() >= contenido.getMaxIntentos()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Ya alcanzaste el maximo de intentos (" + contenido.getMaxIntentos() + ") para este contenido");
        }

        Intento intento = Intento.builder()
                .estudiante(estudiante)
                .contenido(contenido)
                .correctas(req.correctas())
                .total(req.total())
                .numeroIntento(previos.size() + 1)
                .build();

        return IntentoResponse.desde(intentoRepository.save(intento));
    }

    @Transactional(readOnly = true)
    public List<IntentoResponse> misIntentos(Long estudianteId, Long contenidoId) {
        return intentoRepository.findByEstudianteIdAndContenidoId(estudianteId, contenidoId).stream()
                .map(IntentoResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IntentoResponse> porContenido(Long contenidoId) {
        return intentoRepository.findByContenidoId(contenidoId).stream()
                .map(IntentoResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Intento> todos() {
        return intentoRepository.findAllByOrderByFechaDesc();
    }
}
