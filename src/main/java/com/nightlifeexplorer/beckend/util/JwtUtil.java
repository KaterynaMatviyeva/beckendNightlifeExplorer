package com.nightlifeexplorer.beckend.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${spring.jwt.secret}") String secret;

    public String createToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis()+(15*60*1000)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private long jwtExpirationMs;
//
//    public String generateToken(Authentication authentication) {
//        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
//
//        return Jwts.builder()
//                .setSubject(userPrincipal.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//    }

    // Potresti aggiungere metodi per validare il token e per estrarre informazioni se necessario.
}