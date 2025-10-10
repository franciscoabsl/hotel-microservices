package com.capgroup.hotelmicroservices.mspropriedade.service;

import com.capgroup.hotelmicroservices.mspropriedade.dto.requests.PropriedadeRequestDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.PropriedadeResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PropriedadeService {
    PropriedadeResponseDTO create(PropriedadeRequestDTO dto, UUID userId);

    List<PropriedadeResponseDTO> listar();

    PropriedadeResponseDTO buscarPorId(UUID id);

    PropriedadeResponseDTO update(UUID id, PropriedadeRequestDTO dto);

    void delete(UUID id);
}
