package com.capgroup.hotelmicroservices.msreserva.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${app.service.authuser.url}")
    private String authUserUrl;

    @Value("${app.service.propriedade.url}")
    private String propriedadeUrl;

    @Bean
    public WebClient authUserWebClient() {
        return WebClient.builder()
                .baseUrl(authUserUrl)
                .build();
    }

    @Bean
    public WebClient propriedadeWebClient() {
        return WebClient.builder()
                .baseUrl(propriedadeUrl)
                .build();
    }
}