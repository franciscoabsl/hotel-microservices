package com.capgroup.hotelmicroservices.msreserva.adapters.out.http;

import com.capgroup.hotelmicroservices.msreserva.ports.out.AuthUserClient;
import com.capgroup.hotelmicroservices.msreserva.ports.out.dtos.UsuarioDetalheDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.UUID;

@Component
public class AuthUserClientImpl implements AuthUserClient {

    private final WebClient authUserWebClient;

    public AuthUserClientImpl(@Qualifier("authUserWebClient") WebClient authUserWebClient) {
        this.authUserWebClient = authUserWebClient;
    }

    @Override
    public UsuarioDetalheDto getUsuarioDetalhe(UUID userId) {
        return authUserWebClient.get()
                .uri("/usuario/{id}", userId)
                .retrieve()
                // Garante que o WebClient converta a resposta JSON para o DTO correto
                .bodyToMono(UsuarioDetalheDto.class)
                .block();
    }
}
