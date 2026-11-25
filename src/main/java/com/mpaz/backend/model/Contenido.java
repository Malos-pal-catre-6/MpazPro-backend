package com.mpaz.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contenidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contenido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String asignatura;

    @Column(nullable = false)
    private String unidad;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String objetivo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String explicacion;

    @Column(columnDefinition = "TEXT")
    private String ejemplo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoContenido estado = EstadoContenido.BORRADOR;

    @Column(nullable = false)
    @Builder.Default
    private Integer maxIntentos = 3;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "docente_id")
    private Usuario docente;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime creadoEn = LocalDateTime.now();

    @OneToMany(mappedBy = "contenido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Pregunta> preguntas = new ArrayList<>();
}
