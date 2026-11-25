package com.mpaz.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiracion-horas}")
    private long expiracionHoras;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(Long usuarioId, String usuario, String rol) {
        Date ahora = new Date();
        Date expira = new Date(ahora.getTime() + expiracionHoras * 3600_000L);
        return Jwts.builder()
                .subject(usuario)
                .claim("id", usuarioId)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(expira)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraerUsuario(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    public String extraerRol(String token) {
        return extraerClaim(token, c -> c.get("rol", String.class));
    }

    public Long extraerId(String token) {
        return extraerClaim(token, c -> c.get("id", Long.class).longValue());
    }

    public boolean esValido(String token) {
        try {
            return !extraerClaim(token, Claims::getExpiration).before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private <T> T extraerClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }
}
