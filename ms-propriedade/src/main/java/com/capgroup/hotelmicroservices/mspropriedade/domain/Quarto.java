package com.capgroup.hotelmicroservices.mspropriedade.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "propriedade") // Exclui a Propriedade
@Table(name = "quartos")

public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    private String descricao;

    private Double valorDiaria;

    @Enumerated(EnumType.STRING)
    private QuartoStatus status;

    @ManyToOne
    @JoinColumn(name = "propriedade_id")
    private Propriedade propriedade;
}
