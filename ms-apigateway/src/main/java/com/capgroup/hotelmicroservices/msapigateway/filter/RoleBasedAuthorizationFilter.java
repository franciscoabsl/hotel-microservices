package com.capgroup.hotelmicroservices.msapigateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
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
        HttpMethod method  = request.getMethod();

        if (path.contains("/auth/login") || path.contains("/auth/register")) {
            return chain.filter(exchange);
        }

        if (request.getMethod().matches("GET") && (path.startsWith("/propriedades") || (path.startsWith("/quartos")))) {
            return chain.filter(exchange);
        }

        List<String> roles = extractRoles(request.getHeaders().getFirst("X-User-Roles"));

        if (roles.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Role do usuário não informada"));
        }

        if (path.startsWith("/propriedades") && !hasAnyRole(roles, "PROPRIETARIO", "ADMIN")) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para propriedades"));
        }

        if (path.startsWith("/reserva") && !hasAnyRole(roles, "HOSPEDE", "ADMIN")) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para reservas"));
        }

        if (path.startsWith("/reservas/propriedade") && !hasAnyRole(roles, "PROPRIETARIO", "ADMIN")) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado. Apenas proprietário pode listar reservas de sua propriedade."));
        }

        if (path.startsWith("/usuarios") && !request.getMethod().matches("GET") && !hasAnyRole(roles, "HOSPEDE", "ADMIN", "PROPRIETARIO")) {
                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Operação restrita. Apenas ADMIN pode modificar dados de usuário."));
        }


        return chain.filter(exchange);
    }

    private List<String> extractRoles(String roleHeader) {
        if (roleHeader == null || roleHeader.isBlank()) return List.of();
        return List.of(roleHeader.trim());
    }

    private boolean hasAnyRole(List<String> userRoles, String... allowedRoles) {
        return Arrays.stream(allowedRoles).anyMatch(userRoles::contains);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}