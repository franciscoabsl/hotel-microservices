package com.capgroup.hotelmicroservices.mspropriedade.service;


import com.capgroup.hotelmicroservices.mspropriedade.dto.requests.QuartoRequestDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.QuartoResponseDTO;

import java.util.List;

public interface QuartoService {

    QuartoResponseDTO create(Long propriedadeId, QuartoRequestDTO dto);

    List<QuartoResponseDTO> listarPorPropriedade(Long propriedadeId);

    QuartoResponseDTO buscarPorId(Long propriedadeId, Long quartoId);

    QuartoResponseDTO update(Long propriedadeId, Long quartoId, QuartoRequestDTO dto);

    void deletar(Long propriedadeId, Long quartoId);
}
