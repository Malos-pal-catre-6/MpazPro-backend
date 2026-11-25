package com.mpaz.backend.service;

import com.mpaz.backend.model.Intento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class InformeService {

    private final IntentoService intentoService;
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    // Informe pedagogico simple en CSV: un intento por fila.
    public String generarCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("Estudiante,Asignatura,Unidad,Correctas,Total,Intento N,Fecha\n");

        for (Intento i : intentoService.todos()) {
            sb.append(csv(i.getEstudiante().getNombre())).append(",")
              .append(csv(i.getContenido().getAsignatura())).append(",")
              .append(csv(i.getContenido().getUnidad())).append(",")
              .append(i.getCorrectas()).append(",")
              .append(i.getTotal()).append(",")
              .append(i.getNumeroIntento()).append(",")
              .append(i.getFecha().format(FECHA))
              .append("\n");
        }
        return sb.toString();
    }

    private String csv(String texto) {
        if (texto == null) return "";
        String limpio = texto.replace("\"", "\"\"");
        return "\"" + limpio + "\"";
    }
}
