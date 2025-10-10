package com.capgroup.hotelmicroservices.mspropriedade.controller;

import com.capgroup.hotelmicroservices.mspropriedade.dto.requests.QuartoRequestDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.QuartoResponseDTO;
import com.capgroup.hotelmicroservices.mspropriedade.service.QuartoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{propriedadeId}/quartos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Quartos", description = "API para gerenciamento de quartos")

public class QuartoController {

    private final QuartoService quartoService;

    @PostMapping
    public ResponseEntity<QuartoResponseDTO> criar(
            @PathVariable Long propriedadeId,
            @RequestBody QuartoRequestDTO dto) {
        return ResponseEntity.ok(quartoService.create(propriedadeId, dto));
    }

    @GetMapping
    public ResponseEntity<List<QuartoResponseDTO>> listar(@PathVariable Long propriedadeId) {
        return ResponseEntity.ok(quartoService.listarPorPropriedade(propriedadeId));
    }

    @GetMapping("/{quartoId}")
    public ResponseEntity<QuartoResponseDTO> buscarPorId(
            @PathVariable Long propriedadeId,
            @PathVariable Long quartoId) {
        return ResponseEntity.ok(quartoService.buscarPorId(propriedadeId, quartoId));
    }

    @PutMapping("/{quartoId}")
    public ResponseEntity<QuartoResponseDTO> atualizar(
            @PathVariable Long propriedadeId,
            @PathVariable Long quartoId,
            @RequestBody QuartoRequestDTO dto) {
        return ResponseEntity.ok(quartoService.update(propriedadeId, quartoId, dto));
    }

    @DeleteMapping("/{quartoId}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long propriedadeId,
            @PathVariable Long quartoId) {
        quartoService.deletar(propriedadeId, quartoId);
        return ResponseEntity.noContent().build();
    }
}

