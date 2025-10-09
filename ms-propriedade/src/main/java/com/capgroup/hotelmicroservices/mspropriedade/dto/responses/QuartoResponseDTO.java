package com.capgroup.hotelmicroservices.mspropriedade.dto.responses;

import com.capgroup.hotelmicroservices.mspropriedade.domain.QuartoStatus;

public record QuartoResponseDTO(
        Long id,
        String numeracao,
        String descricao,
        Double valorDiaria,
        QuartoStatus status,
        String nomePropriedade
) {}
