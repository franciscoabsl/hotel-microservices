package com.capgroup.hotelmicroservices.mspropriedade.dto.requests;

import com.capgroup.hotelmicroservices.mspropriedade.domain.QuartoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuartoRequestDTO(
        @Schema(description = "Nome do quarto", example = "101")
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @Schema(description = "Descrição do Quarto", example = "Quarto com vista para o mar")
        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @Schema(description = "Valor da diária", example = "500")
        @NotNull(message = "Valor da diária é obrigatória")
        Double valorDiaria,

        @Schema(description = "Propriedade do quarto", example = "1")
        @NotNull(message = "Propriedade é obrigatória")
        Long propriedadeId,

        @Schema(description = "Status do quarto", example = "DISPONIVEL")
        QuartoStatus status
) {
}