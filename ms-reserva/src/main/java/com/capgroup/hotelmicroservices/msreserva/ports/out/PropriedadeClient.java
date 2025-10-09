package com.capgroup.hotelmicroservices.msreserva.ports.out;

import com.capgroup.hotelmicroservices.msreserva.ports.out.dtos.QuartoDetalheDto;
import java.util.UUID;

public interface PropriedadeClient {
    QuartoDetalheDto getQuartoDetalhe(UUID quartoId);
}
