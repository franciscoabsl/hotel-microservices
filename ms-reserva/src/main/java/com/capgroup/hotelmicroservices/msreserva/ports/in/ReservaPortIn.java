package com.capgroup.hotelmicroservices.msreserva.ports.in;

import com.capgroup.hotelmicroservices.msreserva.adapters.in.dtos.ReservaInputDto;
import com.capgroup.hotelmicroservices.msreserva.adapters.in.dtos.ReservaResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ReservaPortIn {

    ResponseEntity<ReservaResponseDto> criarReserva(
            UUID quartoId,
            ReservaInputDto inputDto,
            UUID userId
    );

    ResponseEntity<ReservaResponseDto> buscarReserva(
            UUID reservaId,
            UUID userId,
            String userRoles
    );

    ResponseEntity<Void> cancelarReserva(
            UUID reservaId,
            UUID userId
    );

    ResponseEntity<ReservaResponseDto> atualizarReserva(
            UUID reservaId,
            ReservaInputDto inputDto,
            UUID userId
    );
}