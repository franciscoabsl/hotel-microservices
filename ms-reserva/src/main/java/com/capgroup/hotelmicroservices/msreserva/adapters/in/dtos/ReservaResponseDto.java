package com.capgroup.hotelmicroservices.msreserva.adapters.in.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ReservaResponseDto(

        // Informações da Reserva
        UUID id,
        UUID quartoId,
        LocalDate checkIn,
        LocalDate checkOut,
        BigDecimal valorTotal,

        // Dados do Hóspede (apenas nome)
        String nomeHospede,
        UUID hospedeId,
        String emailHospede,

        // Informações da Propriedade (Agregadas)
        UUID propriedadeId,
        String nomePropriedade,
        String nomeQuarto,

        // Informações do Proprietário (apenas nome, para não expor e-mail)
        String nomeProprietario,
        UUID proprietarioId
) {}
