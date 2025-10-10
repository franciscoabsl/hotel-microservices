package com.capgroup.hotelmicroservices.mspropriedade.dto.responses;

import java.util.UUID;

public record QuartoCompletoResponseDTO(
        UUID id,
        String descricao,
        String nomeQuarto,
        Double valorDiaria,
        UUID propriedadeId,
        String nomePropriedade,
        UUID proprietarioId
) {}
