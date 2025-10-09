package com.capgroup.hotelmicroservices.msauthuser.dto;

/**
 * DTO de resposta para autenticação contendo o token JWT e metadados.
 */
public record AuthTokenResponse(
        String token,
        String tokenType,
        long expiresInSeconds
) {
    public static AuthTokenResponse bearer(String token, long expiresInSeconds) {
        return new AuthTokenResponse(token, "Bearer", expiresInSeconds);
    }
}
