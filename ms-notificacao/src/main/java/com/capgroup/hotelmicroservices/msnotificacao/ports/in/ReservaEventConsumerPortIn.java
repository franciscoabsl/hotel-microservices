package com.capgroup.hotelmicroservices.msnotificacao.ports.in;

import com.capgroup.hotelmicroservices.msnotificacao.ports.out.dtos.ReservaEventoDto;

public interface ReservaEventConsumerPortIn {

    void processarEventoReserva(ReservaEventoDto evento);
}
