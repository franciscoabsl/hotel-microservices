package com.capgroup.hotelmicroservices.msreserva.ports.out;

import com.capgroup.hotelmicroservices.msreserva.core.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, UUID> {

    boolean existsByQuartoIdAndCheckInBeforeAndCheckOutAfter(
            UUID quartoId, LocalDate checkOut, LocalDate checkIn);

    /**
     * Verifica se existe alguma reserva para o quarto e período especificados,
     * excluindo reservas canceladas.
     * Conflito: (checkIn_Existente < checkOut_Novo) AND (checkOut_Existente > checkIn_Novo)
     */
    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END 
        FROM Reserva r 
        WHERE r.quartoId = :quartoId 
          AND r.status <> :statusCancelado 
          AND (r.checkIn < :checkOutNovo) 
          AND (r.checkOut > :checkInNovo)
        """)
    boolean existsByQuartoIdAndCheckInBeforeAndCheckOutAfterAndStatusNot(
            @Param("quartoId") UUID quartoId,
            @Param("checkOutNovo") LocalDate checkOut,
            @Param("checkInNovo") LocalDate checkIn,
            @Param("statusCancelado") String statusCancelado);


    // --- 2. Consultas para ATUALIZAÇÃO (Excluindo a própria Reserva) ---
    /**
     * Verifica se o NOVO período de reserva entra em conflito com QUALQUER OUTRA reserva,
     * excluindo a reserva que está sendo atualizada (idNot) e as canceladas.
     * Conflito: WHERE quartoId = :quartoId AND id != :reservaId AND status != :statusCancelado AND (conflito de datas)
     */
    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END 
        FROM Reserva r 
        WHERE r.quartoId = :quartoId 
          AND r.id <> :reservaId 
          AND r.status <> :statusCancelado 
          AND (r.checkIn < :checkOutNovo) 
          AND (r.checkOut > :checkInNovo)
        """)
    boolean existsByQuartoIdAndIdNotAndCheckInBeforeAndCheckOutAfterAndStatusNot(
            @Param("quartoId") UUID quartoId,
            @Param("reservaId") UUID reservaId,
            @Param("checkOutNovo") LocalDate checkOut,
            @Param("checkInNovo") LocalDate checkIn,
            @Param("statusCancelado") String statusCancelado);

    // --- 3. Consultas para AGENDAMENTO (@Scheduled - Lembretes) ---
    /**
     * Busca todas as reservas cujo check-in coincide com a data especificada
     * (dataLembrete) e que não foram canceladas.
     */
    @Query("""
        SELECT r 
        FROM Reserva r 
        WHERE r.checkIn = :checkInDate 
          AND r.status <> :statusCancelado
        """)
    List<Reserva> findByCheckInAndStatusNot(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("statusCancelado") String statusCancelado);

    List<Reserva> findAllByPropriedadeId(UUID propriedadeId);
}