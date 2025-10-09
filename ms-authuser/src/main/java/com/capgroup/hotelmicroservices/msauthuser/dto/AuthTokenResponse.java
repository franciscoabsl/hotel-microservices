package com.capgroup.hotelmicroservices.msauthuser.dto;
public record AuthTokenResponse(
        String token,
        String tokenType,
        long expiresInSeconds
) {
    public static AuthTokenResponse bearer(String token, long expiresInSeconds) {
        return new AuthTokenResponse(token, "Bearer", expiresInSeconds);
    }
}
