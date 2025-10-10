package com.capgroup.hotelmicroservices.mspropriedade.dto.responses;

import com.capgroup.hotelmicroservices.mspropriedade.domain.TipoPropriedade;

import java.util.List;

public record PropriedadeResponseDTO(
        Long id,
        String nome,
        String descricao,
        TipoPropriedade tipo,
        EnderecoResponseDTO endereco,
        List<QuartoResponseDTO> quartos
) {}
