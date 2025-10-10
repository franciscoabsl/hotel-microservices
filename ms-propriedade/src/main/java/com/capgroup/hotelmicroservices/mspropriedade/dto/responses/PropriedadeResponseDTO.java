package com.capgroup.hotelmicroservices.mspropriedade.dto.responses;

import com.capgroup.hotelmicroservices.mspropriedade.domain.TipoPropriedade;

import java.util.List;
import java.util.UUID;

public record PropriedadeResponseDTO(
        UUID id,
        String nome,
        String descricao,
        TipoPropriedade tipo,
        EnderecoResponseDTO endereco,
        List<QuartoResponseDTO> quartos
) {}
