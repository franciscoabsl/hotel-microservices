//package com.capgroup.hotelmicroservices.msauthuser.security;
//
//import com.capgroup.hotelmicroservices.msauthuser.domain.Perfil;
//import com.capgroup.hotelmicroservices.msauthuser.domain.Usuario;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.junit.jupiter.api.Test;
//
//import javax.crypto.SecretKey;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class JwtServiceTest {
//
//    @Test
//    void generateToken_shouldContainExpectedClaims() {
//        // Secret com 32 bytes (mínimo para HS256)
//        String secret = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; // 32 chars
//        long expSeconds = 60L;
//        JwtService service = new JwtService(secret, expSeconds);
//
//        UUID userId = UUID.randomUUID();
//        Usuario usuario = Usuario.builder()
//                .id(userId)
//                .nome("Teste")
//                .email("teste@example.com")
//                .cpf("12345678901")
//                .senha("hash")
//                .perfil(Perfil.HOSPEDE)
//                .telefone("11999999999")
//                .build();
//
//        String token = service.generateToken(usuario);
//
//        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        assertEquals(usuario.getEmail(), claims.getSubject());
//        assertEquals(userId.toString(), claims.get("userId"));
//        assertEquals(Perfil.HOSPEDE.name(), claims.get("roles"));
//        assertNotNull(claims.getIssuedAt());
//        assertNotNull(claims.getExpiration());
//    }
//}
