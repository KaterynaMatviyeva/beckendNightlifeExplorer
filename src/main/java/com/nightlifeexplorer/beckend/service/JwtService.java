package com.nightlifeexplorer.beckend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {


    @Value("${spring.jwt.secret}")
    private String secretKey;// Sostituisci con una chiave sicura
    private final long jwtExpiration = 86400000; // 24 ore

    // Estrae l'email (sub) dal token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Estrae il ruolo dal token
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Metodo generico per estrarre claims
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Estrae tutti i claims dal token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // Verifica se il token è scaduto
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Estrae la data di scadenza
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().iterator().next().getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Metodi ausiliari (extractUsername, isTokenExpired, extractClaim)...
}