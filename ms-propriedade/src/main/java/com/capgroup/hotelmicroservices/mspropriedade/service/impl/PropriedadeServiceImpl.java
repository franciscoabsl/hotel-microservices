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

@Service
@RequiredArgsConstructor
public class PropriedadeServiceImpl implements PropriedadeService {

    private final PropriedadeRepository propriedadeRepository;
    private final PropriedadeMapper propriedadeMapper;

    @Override
    public PropriedadeResponseDTO create(PropriedadeRequestDTO dto) {
        Propriedade entidade = propriedadeMapper.toEntity(dto);
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
    public PropriedadeResponseDTO buscarPorId(Long id) {
        Propriedade propriedade = propriedadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propriedade não encontrada"));
        return propriedadeMapper.toResponseDTO(propriedade);
    }

    @Override
    public PropriedadeResponseDTO update(Long id, PropriedadeRequestDTO dto) {
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
    public void delete(Long id) {
        propriedadeRepository.deleteById(id);
    }
}