package com.capgroup.hotelmicroservices.msreserva.ports.out.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservaEventoDto(

        UUID id,
        UUID quartoId,
        String nomeQuarto,
        LocalDate checkIn,
        LocalDate checkOut,
        BigDecimal valorTotal,

        UUID hospedeId,
        String nomeHospede,
        String emailHospede,

        UUID proprietarioId,
        String nomeProprietario,
        String emailProprietario,

        String nomePropriedade,
        UUID propriedadeId,

        String tipo, // CRIACAO | ALTERACAO | CANCELAMENTO | LEMBRETE
        LocalDateTime dataEnvio
) {}
