package com.capgroup.hotelmicroservices.msreserva.ports.out.dtos;

import java.util.UUID;

public record QuartoDetalheDto(
        UUID id,
        String descricao,
        String nomeQuarto,
        Double valorDiaria,
        UUID propriedadeId,
        String nomePropriedade,
        UUID proprietarioId
) {}