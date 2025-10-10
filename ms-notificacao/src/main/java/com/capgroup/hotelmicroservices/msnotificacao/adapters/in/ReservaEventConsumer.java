package com.capgroup.hotelmicroservices.msnotificacao.adapters.in;

import com.capgroup.hotelmicroservices.msnotificacao.ports.in.ReservaEventConsumerPortIn;
import com.capgroup.hotelmicroservices.msnotificacao.ports.out.dtos.ReservaEventoDto;
import com.capgroup.hotelmicroservices.msnotificacao.ports.out.NotificacaoSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ReservaEventConsumer implements ReservaEventConsumerPortIn {

    private final NotificacaoSender notificacaoSender;

    public ReservaEventConsumer(NotificacaoSender notificacaoSender) {
        this.notificacaoSender = notificacaoSender;
    }

    /**
     * Ouve a fila configurada e processa o evento de reserva.
     */
    @RabbitListener(queues = "${app.rabbitmq.queue.notificacao}")
    public void receiveReservaEvent(@Payload ReservaEventoDto evento) {
        log.info("Evento de Reserva Recebido. Tipo: {}. ID: {}", evento.tipo(), evento.id());

        try {
            notificacaoSender.enviarNotificacoes(evento);
        } catch (Exception e) {
            log.error("ERRO ao processar notificação para Reserva ID {}. Causa: {}", evento.id(), e.getMessage());
            // A exceção será tratada pela política do Listener (re-try, DLQ)
        }
    }

    @Override
    public void processarEventoReserva(ReservaEventoDto evento) {

    }
}