package com.capgroup.hotelmicroservices.msreserva.adapters.out.message;

import com.capgroup.hotelmicroservices.msreserva.ports.out.dtos.ReservaEventoDto;
import com.capgroup.hotelmicroservices.msreserva.core.domain.Reserva;
import com.capgroup.hotelmicroservices.msreserva.ports.out.RabbitMQSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RabbitMQSenderImpl implements RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;
    private String reservaExchange;

    private static final String ROUTING_KEY_BASE = "reserva.";

    public RabbitMQSenderImpl(@Value("${spring.rabbitmq.exchange.reserva}") String reservaExchange, RabbitTemplate rabbitTemplate) {
        this.reservaExchange = reservaExchange;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override // Sobrescrita obrigatória do método da interface
    public void sendReservaEvent(Reserva reserva, String tipoEvento) {
        // Mapeia a Entidade Rica para o DTO de Evento para o RabbitMQ
        ReservaEventoDto eventoDto = mapToReservaEventoDto(reserva, tipoEvento);

        // A chave de roteamento será: reserva.<TIPO> (ex: reserva.CRIACAO)
        String routingKey = ROUTING_KEY_BASE + tipoEvento.toUpperCase();

        try {
            log.info("Publicando evento: {} na Exchange: {} com chave: {}",
                    tipoEvento, reservaExchange, routingKey);

            rabbitTemplate.convertAndSend(reservaExchange, routingKey, eventoDto);

            log.info("Evento de Reserva ID {} publicado com sucesso.", reserva.getId());
        } catch (Exception e) {
            log.error("Falha ao publicar evento de Reserva ID {} no RabbitMQ. Causa: {}", reserva.getId(), e.getMessage());
            // Consideraríamos um Dead Letter Queue ou uma retry local
        }
    }

    private ReservaEventoDto mapToReservaEventoDto(Reserva reserva, String tipoEvento) {
        // Mapeamento dos campos da Entidade Rica para o Contrato de Evento
        return new ReservaEventoDto(
                reserva.getId(),
                reserva.getQuartoId(),
                reserva.getNomeQuarto(),
                reserva.getCheckIn(),
                reserva.getCheckOut(),
                reserva.getValorTotal(),
                reserva.getHospedeId(),
                reserva.getNomeHospede(),
                reserva.getEmailHospede(),
                reserva.getProprietarioId(),
                reserva.getNomeProprietario(),
                reserva.getEmailProprietario(),
                reserva.getNomePropriedade(),
                reserva.getPropriedadeId(),
                tipoEvento,
                LocalDateTime.now() // Adiciona timestamp de criação do evento
        );
    }
}
