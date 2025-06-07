package com.levanz.customer.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final String secret = "VERY_SECRET_CHANGE_ME";
    private final long   ttlMs  = 24 * 60 * 60 * 1000;   // 24 h

    /* ---------- generation ---------- */
    public String generateToken(String username) {
        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + ttlMs))
                   .signWith(SignatureAlgorithm.HS512, secret)
                   .compact();
    }

    /* ---------- validation ---------- */
    public boolean isTokenValid(String token, String username) {
        return username.equals(extractUser(token)) && !isExpired(token);
    }

    /* ---------- extraction helpers ---------- */
    public String extractUser(String token) {
        return extract(token, Claims::getSubject);
    }
    public Date extractExpiration(String token) {
        return extract(token, Claims::getExpiration);
    }

    private <T> T extract(String token, Function<Claims, T> fn) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return fn.apply(claims);
    }
    private boolean isExpired(String token) { return extractExpiration(token).before(new Date()); }
}
