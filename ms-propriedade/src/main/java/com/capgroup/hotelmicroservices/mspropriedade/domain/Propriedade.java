package com.capgroup.hotelmicroservices.mspropriedade.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "quartos")
@EqualsAndHashCode(exclude = "quartos")

public class Propriedade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private Long proprietarioId;

    @Embedded
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    private TipoPropriedade tipo;

    @OneToMany(mappedBy = "propriedade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quarto> quartos;


    public void adicionarQuarto(Quarto quarto) {
        if (this.quartos == null) {
            this.quartos = new ArrayList<>();
        }
        this.quartos.add(quarto);
        quarto.setPropriedade(this);
    }

    public void removerQuarto(Quarto quarto) {
        this.quartos.remove(quarto);
        quarto.setPropriedade(null);
    }
}
