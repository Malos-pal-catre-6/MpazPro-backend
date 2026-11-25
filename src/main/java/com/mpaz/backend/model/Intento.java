package com.mpaz.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "intentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Intento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_id")
    private Usuario estudiante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contenido_id")
    private Contenido contenido;

    @Column(nullable = false)
    private Integer correctas;

    @Column(nullable = false)
    private Integer total;

    // Numero de intento para ESTE estudiante en ESTE contenido (1, 2, 3...)
    @Column(nullable = false)
    private Integer numeroIntento;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime fecha = LocalDateTime.now();
}
