package com.capgroup.hotelmicroservices.msauthuser.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msAuthuserOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS AuthUser API")
                        .description("API de autenticação e usuários do Hotel Microservices")
                        .version("v1")
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório do projeto")
                        .url("https://github.com/franciscoabsl/hotel-microservices"));
    }
}
