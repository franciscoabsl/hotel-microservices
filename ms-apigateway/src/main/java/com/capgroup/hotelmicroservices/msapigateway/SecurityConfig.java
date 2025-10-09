package com.capgroup.hotelmicroservices.msapigateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // O Gateway usa autenticação JWT, que é stateless (sem estado)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)

                // Desabilita o formulário de login padrão do Spring Security
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                // O Gateway não precisa de regras de autorização aqui;
                // os filtros globais (GlobalFilter) farão o trabalho de Autorização por rota.
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())

                .build();
    }
}
