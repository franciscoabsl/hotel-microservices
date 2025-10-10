package com.capgroup.hotelmicroservices.mspropriedade.controller;

import com.capgroup.hotelmicroservices.mspropriedade.dto.requests.QuartoRequestDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.QuartoCompletoResponseDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.QuartoResponseDTO;
import com.capgroup.hotelmicroservices.mspropriedade.service.QuartoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Quartos", description = "API para gerenciamento de quartos")

public class QuartoController {

    private final QuartoService quartoService;

    @PostMapping("/propriedades/{propriedadeId}/quartos")
    public ResponseEntity<QuartoResponseDTO> criar(
            @PathVariable UUID propriedadeId,
            @RequestBody QuartoRequestDTO dto) {
        return ResponseEntity.ok(quartoService.create(propriedadeId, dto));
    }

    @GetMapping("/propriedades/{propriedadeId}/quartos")
    public ResponseEntity<List<QuartoResponseDTO>> listar(@PathVariable UUID propriedadeId) {
        return ResponseEntity.ok(quartoService.listarPorPropriedade(propriedadeId));
    }

    @GetMapping("/quartos/{quartoId}")
    public ResponseEntity<QuartoResponseDTO> buscarPorId(
            @PathVariable UUID quartoId) {
        return ResponseEntity.ok(quartoService.buscarPorId(quartoId));
    }

    @GetMapping("/quartos/{quartoId}/detalhes")
    public ResponseEntity<QuartoCompletoResponseDTO> buscarPorIdDetalhado(
            @PathVariable UUID quartoId) {
        return ResponseEntity.ok(quartoService.buscarPorIdDetalhado(quartoId));
    }

    @PutMapping("/quartos/{quartoId}")
    public ResponseEntity<QuartoResponseDTO> atualizar(
            @PathVariable UUID propriedadeId,
            @PathVariable UUID quartoId,
            @RequestBody QuartoRequestDTO dto) {
        return ResponseEntity.ok(quartoService.update(propriedadeId, quartoId, dto));
    }

    @DeleteMapping("/quartos/{quartoId}")
    public ResponseEntity<Void> deletar(
            @PathVariable UUID propriedadeId,
            @PathVariable UUID quartoId) {
        quartoService.deletar(propriedadeId, quartoId);
        return ResponseEntity.noContent().build();
    }
}

