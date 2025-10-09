package com.capgroup.hotelmicroservices.msauthuser.security;

import com.capgroup.hotelmicroservices.msauthuser.domain.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationSeconds;

    public JwtService(
            @Value("${security.jwt.secret:change-me-please-change-me-please-change-me-please!}") String secret,
            @Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds
    ) {
        byte[] keyBytes = isBase64(secret) ? Decoders.BASE64.decode(secret) : secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(Usuario usuario) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationSeconds);

    return Jwts.builder()
        .setId(UUID.randomUUID().toString())
        .setSubject(usuario.getEmail())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(exp))
        .claim("userId", usuario.getId().toString())
        .claim("roles", usuario.getPerfil().name())
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    private boolean isBase64(String value) {
        if (value == null || value.isBlank()) return false;
        if ((value.length() % 4) != 0) return false;
        return value.matches("[A-Za-z0-9+/=]+");
    }
}
