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
import java.security.Key;
import java.time.Instant;
import java.util.*;

@Service
public class JwtService {

    private final String secretKey;
    private final long expirationSeconds;

    public JwtService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.expiration-seconds}") long expirationTime) {
        this.secretKey = secret;
        this.expirationSeconds = expirationTime;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Gera o token JWT para o usuário autenticado.
     */
    public String generateToken(Usuario usuario) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", usuario.getId().toString());
        claims.put("userName", usuario.getNome());
        claims.put("roles", usuario.getPerfil().name());
        claims.put("email", usuario.getEmail());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (expirationSeconds * 1000));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
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
