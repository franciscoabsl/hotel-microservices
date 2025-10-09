package com.capgroup.hotelmicroservices.msreserva.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS de Reservas")
                        .description("API para gestão do domínio de reservas, orquestração de dados e lógica de resiliência.")
                        .version("0.0.1-SNAPSHOT"));
    }
}
