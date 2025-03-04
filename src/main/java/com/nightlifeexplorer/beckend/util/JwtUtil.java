package com.nightlifeexplorer.beckend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${spring.jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)   // Aggiunge il claim "role"
                .setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 1000)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .signWith(getSigningKey())
                .compact();
    }


    public boolean validateToken(String token) {
        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
//                    .build()
//                    .parseClaimsJws(token);
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Puoi gestire eccezioni specifiche (ExpiredJwtException, SignatureException, ecc.)
            // e restituire false o rilanciare un'eccezione personalizzata.
            return false;
        }
    }


    public String getSubjectFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            // Se preferisci, puoi lanciare un'eccezione personalizzata
            // throw new BadRequestException("Invalid token");
            return null;
        }
    }
}


