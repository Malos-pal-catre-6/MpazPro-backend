package com.mpaz.backend.service;

import com.mpaz.backend.dto.ContenidoRequest;
import com.mpaz.backend.dto.ContenidoResponse;
import com.mpaz.backend.model.Contenido;
import com.mpaz.backend.model.EstadoContenido;
import com.mpaz.backend.model.Pregunta;
import com.mpaz.backend.model.Usuario;
import com.mpaz.backend.repository.ContenidoRepository;
import com.mpaz.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContenidoService {

    private final ContenidoRepository contenidoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public ContenidoResponse crear(Long docenteId, ContenidoRequest req) {
        Usuario docente = usuarioRepository.findById(docenteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Docente no encontrado"));

        Contenido contenido = Contenido.builder()
                .asignatura(req.asignatura())
                .unidad(req.unidad())
                .objetivo(req.objetivo())
                .explicacion(req.explicacion())
                .ejemplo(req.ejemplo())
                .estado(EstadoContenido.EN_REVISION) // nace pidiendo autorizacion de UTP
                .docente(docente)
                .build();

        req.preguntas().forEach(p -> {
            Pregunta pregunta = Pregunta.builder()
                    .enunciado(p.enunciado())
                    .correcta(p.correcta())
                    .contenido(contenido)
                    .build();
            pregunta.setAlternativas(p.alternativas());
            contenido.getPreguntas().add(pregunta);
        });

        return ContenidoResponse.desde(contenidoRepository.save(contenido));
    }

    @Transactional(readOnly = true)
    public List<ContenidoResponse> misContenidos(Long docenteId) {
        return contenidoRepository.findByDocenteId(docenteId).stream()
                .map(ContenidoResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ContenidoResponse> pendientes() {
        return contenidoRepository.findByEstado(EstadoContenido.EN_REVISION).stream()
                .map(ContenidoResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ContenidoResponse> autorizados() {
        return contenidoRepository.findByEstado(EstadoContenido.AUTORIZADO).stream()
                .map(ContenidoResponse::desde)
                .toList();
    }

    @Transactional
    public ContenidoResponse cambiarEstado(Long contenidoId, String nuevoEstadoTexto) {
        Contenido c = obtener(contenidoId);
        EstadoContenido nuevoEstado;
        try {
            nuevoEstado = EstadoContenido.valueOf(nuevoEstadoTexto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado invalido");
        }
        c.setEstado(nuevoEstado);
        return ContenidoResponse.desde(contenidoRepository.save(c));
    }

    @Transactional
    public ContenidoResponse definirMaxIntentos(Long contenidoId, Long docenteId, int maxIntentos) {
        Contenido c = obtener(contenidoId);
        if (!c.getDocente().getId().equals(docenteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes modificar contenido de otro docente");
        }
        c.setMaxIntentos(maxIntentos);
        return ContenidoResponse.desde(contenidoRepository.save(c));
    }

    public Contenido obtener(Long id) {
        return contenidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contenido no encontrado"));
    }
}
