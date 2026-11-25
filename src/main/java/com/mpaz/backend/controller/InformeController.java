package com.mpaz.backend.controller;

import com.mpaz.backend.service.InformeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/informe")
@RequiredArgsConstructor
public class InformeController {

    private final InformeService informeService;

    // Docente descarga el informe pedagogico como CSV
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportar() {
        byte[] bytes = informeService.generarCsv().getBytes(java.nio.charset.StandardCharsets.UTF_8);

        ContentDisposition disposition = ContentDisposition.attachment()
                .filename("informe_pedagogico.csv")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(bytes);
    }
}
