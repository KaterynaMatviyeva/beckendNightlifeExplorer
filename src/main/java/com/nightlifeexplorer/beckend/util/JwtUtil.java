package com.nightlifeexplorer.beckend.util;

import io.jsonwebtoken.Claims;
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
    @Value("${spring.jwt.secret}")
    private String secret;

    /**
     * Crea un token JWT con subject = email e scadenza di 60 minuti.
     *
     * @param email l'indirizzo email dell'utente (usato come subject)
     * @return il token JWT generato
     */
//    public String createToken(String email) {
//        return Jwts.builder()
//                .setSubject(email)
//                // Imposta scadenza a 60 minuti
//                .setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 1000)))
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
//                .compact();
//    }
    public String createToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)   // Aggiunge il claim "role"
                .setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 1000)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    /**
     * Verifica la validità del token (firma e scadenza).
     * Restituisce true se il token è valido, altrimenti false.
     *
     * @param token JWT da validare
     * @return boolean (true se valido, false altrimenti)
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Puoi gestire eccezioni specifiche (ExpiredJwtException, SignatureException, ecc.)
            // e restituire false o rilanciare un'eccezione personalizzata.
            return false;
        }
    }

    /**
     * Estrae il "subject" (nel nostro caso, l'email) dal token.
     * Se il token non è valido, lancia un'eccezione o restituisce null a seconda della logica scelta.
     *
     * @param token JWT da cui estrarre il subject
     * @return la stringa "subject" (email)
     */
    public String getSubjectFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
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
