package com.capgroup.hotelmicroservices.msreserva.adapters.in.web;

import com.capgroup.hotelmicroservices.msreserva.adapters.in.dtos.ReservaInputDto;
import com.capgroup.hotelmicroservices.msreserva.adapters.in.dtos.ReservaResponseDto;
import com.capgroup.hotelmicroservices.msreserva.core.service.ReservaService;
import com.capgroup.hotelmicroservices.msreserva.ports.in.ReservaPortIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/reservas")
@Slf4j
public class ReservaController implements ReservaPortIn {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PreAuthorize("hasRole('HOSPEDE')")
    @PostMapping("/quarto/{quartoId}")
    public ResponseEntity<ReservaResponseDto> criarReserva(
            @PathVariable UUID quartoId,
            @RequestBody @Valid ReservaInputDto inputDto,
            @RequestHeader("X-User-ID") UUID userId
    ) {
        log.info("Requisição de criação de reserva para Quarto ID: {}. Usuário ID: {}", quartoId, userId);
        ReservaResponseDto novaReserva = reservaService.criarReserva(quartoId, inputDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaReserva);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{reservaId}")
    public ResponseEntity<ReservaResponseDto> buscarReserva(
            @PathVariable UUID reservaId,
            @RequestHeader("X-User-ID") UUID userId,
            @RequestHeader("X-User-Roles") String userRoles
    ) {
        ReservaResponseDto reserva = reservaService.buscarReservaAutorizada(reservaId, userId, userRoles);
        return ResponseEntity.ok(reserva);
    }

    @PreAuthorize("hasAnyRole('HOSPEDE', 'PROPRIETARIO', 'ADMIN')")
    @DeleteMapping("/{reservaId}")
    public ResponseEntity<Void> cancelarReserva(
            @PathVariable UUID reservaId,
            @RequestHeader("X-User-ID") UUID userId
    ) {
        reservaService.cancelarReserva(reservaId, userId);
        log.info("Reserva ID {} cancelada com sucesso pelo usuário ID {}", reservaId, userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyRole('HOSPEDE', 'PROPRIETARIO', 'ADMIN')")
    @PutMapping("/{reservaId}")
    public ResponseEntity<ReservaResponseDto> atualizarReserva(
            @PathVariable("reservaId") UUID reservaId,
            @RequestBody @Valid ReservaInputDto inputDto,
            @RequestHeader("X-User-ID") UUID userId
    ) {
        log.info("Requisição de atualização para Reserva ID: {}", reservaId);
        ReservaResponseDto reservaAtualizada = reservaService.atualizarReserva(reservaId, inputDto, userId);
        return ResponseEntity.ok(reservaAtualizada);
    }
}