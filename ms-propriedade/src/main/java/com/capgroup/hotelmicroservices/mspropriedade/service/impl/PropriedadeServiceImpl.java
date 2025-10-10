package com.capgroup.hotelmicroservices.mspropriedade.service.impl;

import com.capgroup.hotelmicroservices.mspropriedade.domain.Propriedade;
import com.capgroup.hotelmicroservices.mspropriedade.dto.requests.PropriedadeRequestDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.PropriedadeResponseDTO;
import com.capgroup.hotelmicroservices.mspropriedade.mapper.PropriedadeMapper;
import com.capgroup.hotelmicroservices.mspropriedade.repository.PropriedadeRepository;
import com.capgroup.hotelmicroservices.mspropriedade.service.PropriedadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropriedadeServiceImpl implements PropriedadeService {

    private final PropriedadeRepository propriedadeRepository;
    private final PropriedadeMapper propriedadeMapper;

    @Override
    public PropriedadeResponseDTO create(PropriedadeRequestDTO dto, UUID userId) {
        Propriedade entidade = propriedadeMapper.toEntity(dto);
        entidade.setProprietarioId(userId);
        propriedadeRepository.save(entidade);
        return propriedadeMapper.toResponseDTO(entidade);
    }

    @Override
    public List<PropriedadeResponseDTO> listar() {
        return propriedadeRepository.findAll()
                .stream()
                .map(propriedadeMapper::toResponseDTO)
                .toList();
    }

    @Override
    public PropriedadeResponseDTO buscarPorId(UUID id) {
        Propriedade propriedade = propriedadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propriedade não encontrada"));
        return propriedadeMapper.toResponseDTO(propriedade);
    }

    @Override
    public PropriedadeResponseDTO update(UUID id, PropriedadeRequestDTO dto) {
        Propriedade propriedade = propriedadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propriedade não encontrada"));

        propriedade.setNome(dto.nome());
        propriedade.setDescricao(dto.descricao());
        propriedade.setTipo(dto.tipo());
        propriedade.setEndereco(dto.endereco().toEntity());

        propriedadeRepository.save(propriedade);
        return propriedadeMapper.toResponseDTO(propriedade);
    }

    @Override
    public void delete(UUID id) {
        propriedadeRepository.deleteById(id);
    }
}