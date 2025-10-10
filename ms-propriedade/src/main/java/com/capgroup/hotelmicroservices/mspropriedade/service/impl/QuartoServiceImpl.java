package com.capgroup.hotelmicroservices.mspropriedade.service.impl;


import com.capgroup.hotelmicroservices.mspropriedade.domain.Propriedade;
import com.capgroup.hotelmicroservices.mspropriedade.domain.Quarto;
import com.capgroup.hotelmicroservices.mspropriedade.dto.requests.QuartoRequestDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.QuartoResponseDTO;
import com.capgroup.hotelmicroservices.mspropriedade.mapper.QuartoMapper;
import com.capgroup.hotelmicroservices.mspropriedade.repository.PropriedadeRepository;
import com.capgroup.hotelmicroservices.mspropriedade.repository.QuartoRepository;
import com.capgroup.hotelmicroservices.mspropriedade.service.QuartoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuartoServiceImpl implements QuartoService {

    private final QuartoRepository quartoRepository;
    private final PropriedadeRepository propriedadeRepository;
    private final QuartoMapper quartoMapper;

    @Override
    public QuartoResponseDTO create(Long propriedadeId, QuartoRequestDTO dto) {
        Propriedade propriedade = propriedadeRepository.findById(propriedadeId)
                .orElseThrow(() -> new RuntimeException("Propriedade não encontrada"));

        Quarto quarto = quartoMapper.toEntity(dto);
        quarto.setPropriedade(propriedade);

        quartoRepository.save(quarto);
        return quartoMapper.toResponseDTO(quarto);
    }

    @Override
    public List<QuartoResponseDTO> listarPorPropriedade(Long propriedadeId) {
        return quartoRepository.findByPropriedadeId(propriedadeId)
                .stream()
                .map(quartoMapper::toResponseDTO)
                .toList();
    }

    @Override
    public QuartoResponseDTO buscarPorId(Long propriedadeId, Long quartoId) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .filter(q -> q.getPropriedade().getId().equals(propriedadeId))
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));
        return quartoMapper.toResponseDTO(quarto);
    }

    @Override
    public QuartoResponseDTO update(Long propriedadeId, Long quartoId, QuartoRequestDTO dto) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .filter(q -> q.getPropriedade().getId().equals(propriedadeId))
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));

        quarto.setDescricao(dto.descricao());
        quarto.setValorDiaria(dto.valorDiaria());
        quarto.setStatus(dto.status());

        quartoRepository.save(quarto);
        return quartoMapper.toResponseDTO(quarto);
    }

    @Override
    public void deletar(Long propriedadeId, Long quartoId) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .filter(q -> q.getPropriedade().getId().equals(propriedadeId))
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));
        quartoRepository.delete(quarto);
    }
}
