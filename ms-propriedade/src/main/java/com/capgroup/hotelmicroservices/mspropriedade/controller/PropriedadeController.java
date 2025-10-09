package com.capgroup.hotelmicroservices.mspropriedade.controller;


import com.capgroup.hotelmicroservices.mspropriedade.dto.requests.PropriedadeRequestDTO;
import com.capgroup.hotelmicroservices.mspropriedade.dto.responses.PropriedadeResponseDTO;
import com.capgroup.hotelmicroservices.mspropriedade.service.PropriedadeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/propriedades")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Propriedades", description = "API para gerenciamento de propriedades")

public class PropriedadeController {

    private final PropriedadeService propriedadeService;

    @PostMapping
    public ResponseEntity<PropriedadeResponseDTO> criar(@RequestBody PropriedadeRequestDTO dto) {
        return ResponseEntity.ok(propriedadeService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<PropriedadeResponseDTO>> listar() {
        return ResponseEntity.ok(propriedadeService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropriedadeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(propriedadeService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropriedadeResponseDTO> atualizar(@PathVariable Long id, @RequestBody PropriedadeRequestDTO dto) {
        return ResponseEntity.ok(propriedadeService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        propriedadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
