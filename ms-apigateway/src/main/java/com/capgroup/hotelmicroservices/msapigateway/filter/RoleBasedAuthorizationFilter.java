package com.capgroup.hotelmicroservices.msapigateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleBasedAuthorizationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        List<String> roles = extractRoles(request.getHeaders().getFirst("X-User-Roles"));

        if (roles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role do usuário não informada");
        }

        // Regras de acesso por tipo de usuário
        if (path.startsWith("/propriedade") && !hasAnyRole(roles, "proprietario", "admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para propriedades");
        }

        if (path.startsWith("/reserva") && !hasAnyRole(roles, "hospede", "admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para reservas");
        }

        if (path.startsWith("/user") && !path.contains("/login") && !path.contains("/register") && !hasAnyRole(roles, "admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para usuários");
        }

        return chain.filter(exchange);
    }

    private List<String> extractRoles(String roleHeader) {
        if (roleHeader == null || roleHeader.isBlank()) return List.of();
        return Arrays.stream(roleHeader.split(","))
                .map(String::trim)
                .toList();
    }

    private boolean hasAnyRole(List<String> userRoles, String... allowedRoles) {
        return Arrays.stream(allowedRoles).anyMatch(userRoles::contains);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}