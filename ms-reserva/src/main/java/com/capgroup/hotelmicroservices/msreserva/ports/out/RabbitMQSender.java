package com.capgroup.hotelmicroservices.msreserva.ports.out;

import com.capgroup.hotelmicroservices.msreserva.core.domain.Reserva;

public interface RabbitMQSender {
    void sendReservaEvent(Reserva reserva, String tipoEvento);
}
