package com.capgroup.hotelmicroservices.msreserva.adapters.in.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class HeaderAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String userIdHeader = request.getHeader("X-User-ID");
        String rolesHeader = request.getHeader("X-User-Roles");

        if (userIdHeader != null && !userIdHeader.isEmpty() && rolesHeader != null && !rolesHeader.isEmpty()) {
            try {
                UUID userId = UUID.fromString(userIdHeader);

                // 1. Cria a identidade com as roles
                UserDetailsImpl userDetails = new UserDetailsImpl(userId, rolesHeader);

                // 2. Cria o token de autenticação (usuário já autenticado pelo Gateway)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 3. Coloca a identidade no contexto do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (IllegalArgumentException e) {
                // Logar erro de formato, mas continuar para o FilterChain
            }
        }

        filterChain.doFilter(request, response);
    }
}
