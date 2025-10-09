package com.capgroup.hotelmicroservices.mspropriedade.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI msPropriedadecustomOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microserviço de Propriedades e Quartos")
                        .description("API responsável pelo gerenciamento de propriedades e quartos no sistema de hotel")
                        .version("v2"));
    }
}
