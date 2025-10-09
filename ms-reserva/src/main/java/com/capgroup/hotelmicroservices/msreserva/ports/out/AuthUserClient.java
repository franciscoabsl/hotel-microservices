package com.capgroup.hotelmicroservices.msreserva.ports.out;

import com.capgroup.hotelmicroservices.msreserva.ports.out.dtos.UsuarioDetalheDto;
import java.util.UUID;

public interface AuthUserClient {
    UsuarioDetalheDto getUsuarioDetalhe(UUID userId);
}
