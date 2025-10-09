package com.capgroup.hotelmicroservices.msreserva.core.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_RESERVAS")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Dados base da Reserva
    private UUID quartoId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BigDecimal valorTotal;
    private ReservaStatusEnum status;

    // Dados do Hóspede (Agregados do Auth-User MS)
    private UUID hospedeId;
    private String nomeHospede;
    private String emailHospede;

    // Dados da Propriedade (Agregados do Propriedade MS)
    private UUID propriedadeId;
    private String nomePropriedade;
    private String nomeQuarto;

    // Dados do Proprietário (Agregados do Propriedade MS ou Auth-User MS)
    private UUID proprietarioId;
    private String nomeProprietario;
    private String emailProprietario;
}

public enum ReservaStatusEnum {
    ATIVA,
    CANCELADA,
    CONCLUIDA
}