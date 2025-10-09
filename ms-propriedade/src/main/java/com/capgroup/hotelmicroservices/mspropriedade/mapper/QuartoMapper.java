package com.capgroup.hotelmicroservices.mspropriedade.mapper;

import com.capgroup.hotelmicroservices.mspropriedade.domain.Quarto;
import com.capgroup.hotelmicroservices.mspropriedade.dto.requests.QuartoRequestDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.QuartoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class QuartoMapper {

    public Quarto toEntity(QuartoRequestDTO dto) {
        return Quarto.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .valorDiaria(dto.valorDiaria())
                .status(dto.status())
                .build();
    }

    public QuartoResponseDTO toResponseDTO(Quarto entity) {
        String nomePropriedade = entity.getPropriedade() != null ? entity.getPropriedade().getNome() : "N/A";

        return new QuartoResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getValorDiaria(),
                entity.getStatus(),
                nomePropriedade
        );
    }
}