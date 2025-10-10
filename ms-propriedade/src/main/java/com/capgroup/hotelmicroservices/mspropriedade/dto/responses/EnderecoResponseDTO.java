package com.capgroup.hotelmicroservices.mspropriedade.dto.responses;

import com.capgroup.hotelmicroservices.mspropriedade.domain.Endereco;

public record EnderecoResponseDTO(
        String rua,
        String bairro,
        String cidade,
        String estado
) {
    public EnderecoResponseDTO(Endereco endereco) {
        this(endereco.getRua(), endereco.getBairro(), endereco.getCidade(), endereco.getEstado());
    }
}
