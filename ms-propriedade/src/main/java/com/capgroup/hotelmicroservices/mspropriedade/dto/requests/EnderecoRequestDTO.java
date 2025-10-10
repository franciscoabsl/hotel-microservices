package com.capgroup.hotelmicroservices.mspropriedade.dto.requests;

import com.capgroup.hotelmicroservices.mspropriedade.domain.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record EnderecoRequestDTO(
        @Schema(description = "Nome da rua", example = "Avenida Atlântica")
        @NotBlank(message = "Rua é obrigatória") String rua,

        @Schema(description = "Nome do bairro", example = "Copacabana")
        @NotBlank(message = "Bairro é obrigatório") String bairro,

        @Schema(description = "Nome da cidade", example = "Rio de Janeiro")
        @NotBlank(message = "Cidade é obrigatória") String cidade,

        @Schema(description = "Nome do estado", example = "RJ")
        @NotBlank(message = "Estado é obrigatório") String estado
) {
    public Endereco toEntity() {
        return new Endereco(rua, bairro, cidade, estado);
    }
}
