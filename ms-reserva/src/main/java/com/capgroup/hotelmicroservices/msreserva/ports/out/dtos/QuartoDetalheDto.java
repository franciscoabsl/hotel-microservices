package com.capgroup.hotelmicroservices.msreserva.ports.out.dtos;

import java.math.BigDecimal;
import java.util.UUID;

// Dados ricos do Quarto, agregando informações do Proprietário e Propriedade
public record QuartoDetalheDto(
        UUID id,
        BigDecimal valorDiaria,
        String nomeQuarto,
        String nomePropriedade,

        // Campo crítico para Autorização e Evento Rico
        UUID proprietarioId
) {}