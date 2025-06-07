package com.levanz.customer.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String base64Secret;

    private Key key;

    private static final long TTL_MS = 24 * 60 * 60 * 1000L;

    @PostConstruct
    private void initKey() {
        // Decode only once; if the property is wrong the app fails fast here
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    }

    /* ── token generation ────────────────────────────────────────── */
    public String generateToken(Authentication auth) {
        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("authorities",
                       auth.getAuthorities().stream()
                           .map(GrantedAuthority::getAuthority)
                           .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TTL_MS))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /* ── validation / extraction ─────────────────────────────────── */
    public boolean isTokenValid(String token, String username) {
        return username.equals(extractUsername(token)) && !isExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<String> extractAuthorities(String token) {
        return extractAllClaims(token).get("authorities", List.class);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> fn) {
        return fn.apply(extractAllClaims(token));
    }

    /* ── helpers ─────────────────────────────────────────────────── */
    private boolean isExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }
}
