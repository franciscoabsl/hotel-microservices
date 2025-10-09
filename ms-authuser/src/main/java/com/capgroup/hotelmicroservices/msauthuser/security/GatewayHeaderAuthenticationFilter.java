package com.capgroup.hotelmicroservices.msauthuser.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Autentica requisições com base nos headers enviados pelo API Gateway.
 * Espera-se que o Gateway valide o JWT e propague:
 * - X-User-ID: UUID do usuário
 * - X-User-Roles: lista separada por vírgula (ex.: "ADMIN" ou "HOSPEDE,PROPRIETARIO")
 */
@Component
public class GatewayHeaderAuthenticationFilter extends OncePerRequestFilter {

    public static final String HEADER_USER_ID = "X-User-ID";
    public static final String HEADER_USER_ROLES = "X-User-Roles";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Ignorar endpoints públicos de autenticação
        String path = request.getRequestURI();
        if (isPublicAuthEndpoint(request.getMethod(), path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String userId = request.getHeader(HEADER_USER_ID);
        String rolesHeader = request.getHeader(HEADER_USER_ROLES);

        // Só autentica se ambos os headers existirem e houver pelo menos uma role válida
        if (userId != null && !userId.isBlank() && rolesHeader != null && !rolesHeader.isBlank()) {
            Collection<? extends GrantedAuthority> authorities = parseAuthorities(rolesHeader);
            if (!authorities.isEmpty()) {
                var auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicAuthEndpoint(String method, String path) {
        if (path == null) return false;
        String normalized = path.toLowerCase(Locale.ROOT);
        boolean isAuthBase = normalized.startsWith("/auth/");
        boolean isAuthRoot = normalized.equals("/auth") || normalized.equals("/auth/");
        boolean isAuth = isAuthBase || isAuthRoot;
        return isAuth && (HttpMethod.POST.matches(method) || HttpMethod.OPTIONS.matches(method));
    }

    private Collection<? extends GrantedAuthority> parseAuthorities(String rolesHeader) {
        if (rolesHeader == null || rolesHeader.isBlank()) return java.util.List.of();
        return Arrays.stream(rolesHeader.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(r -> r.toUpperCase(Locale.ROOT))
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());
    }
}
