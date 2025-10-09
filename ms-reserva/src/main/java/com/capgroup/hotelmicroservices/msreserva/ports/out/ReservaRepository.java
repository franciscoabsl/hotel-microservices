package com.capgroup.hotelmicroservices.msreserva.ports.out;

import com.capgroup.hotelmicroservices.msreserva.core.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, UUID> {

    boolean existsByQuartoIdAndCheckInBeforeAndCheckOutAfter(
            UUID quartoId, LocalDate checkOut, LocalDate checkIn);

    /**
     * Método usado para ATUALIZAR Reserva.
     * Verifica se há conflito de datas para um quarto, EXCLUINDO o ID da reserva que está sendo atualizada.
     * Lógica SQL: WHERE quarto_id = :quartoId AND id != :reservaId AND (conflito de datas)
     */
        @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END 
            FROM Reserva r 
            WHERE r.quartoId = :quartoId
              AND r.id != :reservaId 
              AND r.checkIn < :checkOut
              AND r.checkOut > :checkIn
        """)
        boolean existsConflictByQuartoIdExcludingId(
                @Param("quartoId") UUID quartoId,
                @Param("reservaId") UUID reservaId,
                @Param("checkIn") LocalDate checkIn,
                @Param("checkOut") LocalDate checkOut
        );
    }