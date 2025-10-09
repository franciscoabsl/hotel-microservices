package com.capgroup.hotelmicroservices.mspropriedade.mapper;

import com.capgroup.hotelmicroservices.mspropriedade.domain.Propriedade;
import com.capgroup.hotelmicroservices.mspropriedade.dto.requests.PropriedadeRequestDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.EnderecoResponseDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.PropriedadeResponseDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.QuartoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PropriedadeMapper {

    private final QuartoMapper quartoMapper;

    public Propriedade toEntity(PropriedadeRequestDTO dto) {
        Propriedade propriedade = new Propriedade();
        propriedade.setNome(dto.nome());
        propriedade.setDescricao(dto.descricao());
        propriedade.setTipo(dto.tipo());

        if (dto.endereco() != null) {
            propriedade.setEndereco(dto.endereco().toEntity());
        }

        return propriedade;
    }

    public PropriedadeResponseDTO toResponseDTO(Propriedade entity) {
        EnderecoResponseDTO enderecoDto = entity.getEndereco() != null
                ? new EnderecoResponseDTO(entity.getEndereco())
                : null;

        List<QuartoResponseDTO> quartosDto = entity.getQuartos().stream()
                .map(quartoMapper::toResponseDTO)
                .toList();

        return new PropriedadeResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getTipo(),
                enderecoDto,
                quartosDto
        );
    }
}