package com.mpaz.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "preguntas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    // Se guarda como texto separado por "||" para evitar otra tabla (simple y suficiente).
    @Column(nullable = false, columnDefinition = "TEXT")
    private String alternativasRaw;

    @Column(nullable = false)
    private Integer correcta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contenido_id")
    private Contenido contenido;

    @Transient
    public List<String> getAlternativas() {
        return List.of(alternativasRaw.split("\\|\\|"));
    }

    public void setAlternativas(List<String> alternativas) {
        this.alternativasRaw = String.join("||", alternativas);
    }
}
