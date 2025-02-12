package com.nightlifeexplorer.beckend.controller;

import com.nightlifeexplorer.beckend.dto.LoginRequest;
import com.nightlifeexplorer.beckend.dto.LoginResponse;
import com.nightlifeexplorer.beckend.dto.RegisterRequest;
import com.nightlifeexplorer.beckend.entity.Role;
import com.nightlifeexplorer.beckend.entity.User;
import com.nightlifeexplorer.beckend.repository.UserRepository;
import com.nightlifeexplorer.beckend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // Endpoint di registrazione con validazione e controllo duplicati
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        // Controllo se il nome utente esiste già
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Nome utente già in uso");
        }

        // Creazione dell'utente e impostazione dei dati
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        // Imposta un ruolo di default (es. ROLE_USER) oppure valuta in base alla logica del business
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);
        return ResponseEntity.ok("Registrazione avvenuta con successo!");
    }

    // Endpoint di login: autentica l'utente e restituisce un JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            // Genera il token JWT
            String token = jwtUtil.generateToken(authentication);
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Nome utente o password non validi");
        }
    }

}