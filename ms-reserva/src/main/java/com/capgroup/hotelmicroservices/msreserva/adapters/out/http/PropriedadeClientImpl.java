package com.capgroup.hotelmicroservices.msreserva.adapters.out.http;

import com.capgroup.hotelmicroservices.msreserva.ports.out.PropriedadeClient;
import com.capgroup.hotelmicroservices.msreserva.ports.out.dtos.QuartoDetalheDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.UUID;

@Component
public class PropriedadeClientImpl implements PropriedadeClient {

    private final WebClient propriedadeWebClient;

    public PropriedadeClientImpl(@Qualifier("propriedadeWebClient") WebClient propriedadeWebClient) {
        this.propriedadeWebClient = propriedadeWebClient;
    }

    @Override
    public QuartoDetalheDto getQuartoDetalhe(UUID quartoId) {
        return propriedadeWebClient.get()
                .uri("/propriedades/quarto/{id}", quartoId)
                .retrieve()
                // Garante que o WebClient converta a resposta JSON para o DTO correto
                .bodyToMono(QuartoDetalheDto.class)
                .block();
    }
}
