package com.capgroup.hotelmicroservices.mspropriedade.service.impl;


import com.capgroup.hotelmicroservices.mspropriedade.domain.Propriedade;
import com.capgroup.hotelmicroservices.mspropriedade.domain.Quarto;
import com.capgroup.hotelmicroservices.mspropriedade.dto.requests.QuartoRequestDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.QuartoCompletoResponseDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.QuartoResponseDTO;
import com.capgroup.hotelmicroservices.mspropriedade.mapper.QuartoMapper;
import com.capgroup.hotelmicroservices.mspropriedade.repository.PropriedadeRepository;
import com.capgroup.hotelmicroservices.mspropriedade.repository.QuartoRepository;
import com.capgroup.hotelmicroservices.mspropriedade.service.QuartoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuartoServiceImpl implements QuartoService {

    private final QuartoRepository quartoRepository;
    private final PropriedadeRepository propriedadeRepository;
    private final QuartoMapper quartoMapper;

    @Override
    public QuartoResponseDTO create(UUID propriedadeId, QuartoRequestDTO dto) {
        Propriedade propriedade = propriedadeRepository.findById(propriedadeId)
                .orElseThrow(() -> new RuntimeException("Propriedade não encontrada"));

        Quarto quarto = quartoMapper.toEntity(dto);
        quarto.setPropriedade(propriedade);

        quartoRepository.save(quarto);
        return quartoMapper.toResponseDTO(quarto);
    }

    @Override
    public List<QuartoResponseDTO> listarPorPropriedade(UUID propriedadeId) {
        return quartoRepository.findByPropriedadeId(propriedadeId)
                .stream()
                .map(quartoMapper::toResponseDTO)
                .toList();
    }

    @Override
    public QuartoResponseDTO buscarPorId(UUID quartoId) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));
        return quartoMapper.toResponseDTO(quarto);
    }

    @Override
    public QuartoResponseDTO update(UUID propriedadeId, UUID quartoId, QuartoRequestDTO dto) {
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
    public void deletar(UUID propriedadeId, UUID quartoId) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .filter(q -> q.getPropriedade().getId().equals(propriedadeId))
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));
        quartoRepository.delete(quarto);
    }

    @Override
    public QuartoCompletoResponseDTO buscarPorIdDetalhado(UUID quartoId) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));

        Propriedade propriedade = propriedadeRepository.findById(quarto.getPropriedade().getId())
                .orElseThrow(() -> new RuntimeException("Propriedade não encontrada"));

        return new QuartoCompletoResponseDTO(
                quarto.getId(),
                quarto.getDescricao(),
                quarto.getNome(),
                quarto.getValorDiaria(),
                propriedade.getId(),
                propriedade.getNome(),
                propriedade.getProprietarioId()
        );
    }
}
