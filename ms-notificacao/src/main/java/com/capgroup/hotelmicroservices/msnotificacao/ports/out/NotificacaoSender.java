package com.capgroup.hotelmicroservices.msnotificacao.ports.out;

import com.capgroup.hotelmicroservices.msnotificacao.ports.out.dtos.ReservaEventoDto;

public interface NotificacaoSender {
    void enviarNotificacoes(ReservaEventoDto evento);
}