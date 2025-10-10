package com.capgroup.hotelmicroservices.mspropriedade.service;

import com.capgroup.hotelmicroservices.mspropriedade.dto.requests.PropriedadeRequestDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.PropriedadeResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PropriedadeService {
    PropriedadeResponseDTO create(PropriedadeRequestDTO dto);

    List<PropriedadeResponseDTO> listar();

    PropriedadeResponseDTO buscarPorId(Long id);

    PropriedadeResponseDTO update(Long id, PropriedadeRequestDTO dto);

    void delete(Long id);
}
