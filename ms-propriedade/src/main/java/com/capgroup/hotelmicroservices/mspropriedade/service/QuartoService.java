package com.capgroup.hotelmicroservices.mspropriedade.service;


import com.capgroup.hotelmicroservices.mspropriedade.dto.requests.QuartoRequestDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.QuartoCompletoResponseDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.QuartoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface QuartoService {

    QuartoResponseDTO create(UUID propriedadeId, QuartoRequestDTO dto);

    List<QuartoResponseDTO> listarPorPropriedade(UUID propriedadeId);

    QuartoResponseDTO buscarPorId(UUID quartoId);

    QuartoResponseDTO update(UUID propriedadeId, UUID quartoId, QuartoRequestDTO dto);

    void deletar(UUID propriedadeId, UUID quartoId);

    QuartoCompletoResponseDTO buscarPorIdDetalhado(UUID quartoId);
}
