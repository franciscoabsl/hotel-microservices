package com.capgroup.hotelmicroservices.msreserva.adapters.in.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservaInputDto(

        @Schema(
                description = "Data de início da reserva (check-in). Deve ser no formato AAAA-MM-DD.",
                example = "2025-11-20"
        )
        @NotNull(message = "A data de check-in é obrigatória.")
        @FutureOrPresent(message = "A data de check-in deve ser no presente ou futuro.")
        LocalDate checkIn,

        @Schema(
                description = "Data de término da reserva (check-out). Deve ser posterior ao check-in.",
                example = "2025-11-25"
        )
        @NotNull(message = "A data de check-out é obrigatória.")
        @FutureOrPresent(message = "A data de check-out deve ser no presente ou futuro.")
        LocalDate checkOut
) {}