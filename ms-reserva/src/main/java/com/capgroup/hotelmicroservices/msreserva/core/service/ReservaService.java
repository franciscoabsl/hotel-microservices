package com.capgroup.hotelmicroservices.msreserva.core.service;

import com.capgroup.hotelmicroservices.msreserva.adapters.in.dtos.ReservaInputDto;
import com.capgroup.hotelmicroservices.msreserva.adapters.in.dtos.ReservaResponseDto;
import com.capgroup.hotelmicroservices.msreserva.core.domain.Reserva;

import java.util.UUID;

public interface ReservaService {

    ReservaResponseDto criarReserva(UUID quartoId, ReservaInputDto inputDto, UUID userId);

    ReservaResponseDto atualizarReserva(UUID reservaId, ReservaInputDto inputDto, UUID userId);

    ReservaResponseDto cancelarReserva(UUID reservaId, UUID userId);

    Reserva buscarReservaPorId(UUID reservaId);

    ReservaResponseDto buscarReservaAutorizada(UUID reservaId, UUID userId, String userRoles);

    Reserva buscarReservaParaAtualizacao(UUID reservaId, UUID userId, String userRoles);

}