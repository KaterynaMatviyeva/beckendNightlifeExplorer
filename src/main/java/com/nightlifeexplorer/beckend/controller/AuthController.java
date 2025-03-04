package com.nightlifeexplorer.beckend.controller;

import com.nightlifeexplorer.beckend.dto.*;

import com.nightlifeexplorer.beckend.entity.User;
import com.nightlifeexplorer.beckend.enums.APIStatus;
import com.nightlifeexplorer.beckend.enums.Role;
import com.nightlifeexplorer.beckend.exception.BadRequestException;


import com.nightlifeexplorer.beckend.service.UserService;
import com.nightlifeexplorer.beckend.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:4200")
//@RequiredArgsConstructor
public class AuthController {


    @Autowired
    UserService userSvr;

    @Autowired
    JwtUtil jwt;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//    @Autowired
//    private PasswordEncoder passwordEncoder;



    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseGeneric<AuthResponseDTO> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(
                    validation.getAllErrors()
                            .stream()
                            .map(err -> err.getDefaultMessage())
                            .collect(Collectors.joining(", "))
            );
        }

        // Verifica se l'email è già registrata
        if (userSvr.emailExists(registerRequest.getEmail())) {
            throw new BadRequestException("Email già registrata");
        }

        // Gestione del ruolo: default ROLE_USER se non specificato
        Role role = Role.ROLE_USER;
        if (registerRequest.getRole() != null && !registerRequest.getRole().isBlank()) {
            try {
                role = Role.valueOf(registerRequest.getRole());
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Ruolo non valido");
            }
        }
    //password cittografata
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // Crea il nuovo utente
        User newUser = new User(registerRequest.getEmail(), registerRequest.getUsername(), encodedPassword, role);
        userSvr.save(newUser);

        User savedUser = userSvr.save(newUser);
        if (savedUser == null) {
            throw new BadRequestException("Errore durante la registrazione dell'utente");
        }
        String token = jwt.createToken(newUser.getEmail(), newUser.getRole().name());

        UserDTO userDTO = new UserDTO(newUser.getId(), newUser.getUsername(), newUser.getEmail(), newUser.getRole());

        AuthResponseDTO data = new AuthResponseDTO(userDTO, token);

        return new LoginResponseGeneric<>(
                APIStatus.SUCCESS,
                data,
                null
        );
    }






    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseGeneric getMe(HttpServletRequest request) {

        // 1. Recupera l’header "Authorization"
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Se non c'è l'header o non inizia con "Bearer ", l'utente non è autenticato
            throw new BadRequestException("Missing or invalid Authorization header");

        }

        // 2. Estrarre il token JWT (senza la stringa "Bearer ")
        String token = authHeader.substring(7);

        // 3. Verifica se il token è valido
        if (!jwt.validateToken(token)) {
            throw new BadRequestException("Invalid token");
            // O un'altra eccezione per gestire la 401
        }

        // 4. Ricava l'email dal token
        String email = jwt.getSubjectFromToken(token);

        // 5. Trova l'utente nel database usando il service
        User found = this.userSvr.findByEmail(email);

        // 6. Crea il DTO
        UserDTO userDTO = new UserDTO(
                found.getId(),
                found.getEmail(),
                found.getUsername(),
                found.getRole()
        );
        LoginResponseDTO loginResponse = new LoginResponseDTO(userDTO, token);
        // 7. Restituisci la risposta con i dati dell’utente
        return new LoginResponseGeneric<>(
                APIStatus.SUCCESS,
                loginResponse,
                null
        );
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseGeneric login(@RequestBody LoginRequest credentials, BindingResult validation){
        if(validation.hasErrors()) throw new BadRequestException(
                validation.getAllErrors()
                        .stream()
                        .map(err->err.getDefaultMessage()).toString());
        User found = this.userSvr.findByEmail(credentials.email());
        if (passwordEncoder.matches(credentials.password(), found.getPassword())) {
            try {
                String token = jwt.createToken(credentials.email(), found.getRole().name());

                UserDTO userDTO = new UserDTO(found.getId(), found.getEmail(), found.getUsername(), found.getRole());

                LoginResponseDTO loginResponse = new LoginResponseDTO(userDTO, token);

                return new LoginResponseGeneric<>(APIStatus.SUCCESS, loginResponse, null);
            } catch (Exception e) {
                throw new BadRequestException("Errore nella generazione del token JWT");
            }
//                    return new LoginResponse<TokenDTO>(APIStatus.SUCCESS, new TokenDTO(jwt.createToken(credentials.email())) , null);

                }else throw new BadRequestException("Wrong password");
    }

    // Aggiorna utente esistente
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseGeneric<?> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest updateUserRequest,
            BindingResult validation
    ) {
        // Se ci sono errori di validazione, lanciamo eccezione
        if (validation.hasErrors()) {
            throw new BadRequestException(
                    validation.getAllErrors()
                            .stream()
                            .map(err -> err.getDefaultMessage())
                            .collect(Collectors.joining(", "))
            );
        }

        // 1. Trova l'utente
        User foundUser = userSvr.findById(id);
        if (foundUser == null) {
            throw new BadRequestException("Utente non trovato");
        }

        // 2. Aggiorna i campi che vuoi modificare (se non null)
        if (updateUserRequest.getEmail() != null) {
            foundUser.setEmail(updateUserRequest.getEmail());
        }

        if (updateUserRequest.getUsername() != null) {
            foundUser.setUsername(updateUserRequest.getUsername());
        }

        if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().isBlank()) {
            // Ricordati di criptare la password
            String encodedPassword = passwordEncoder.encode(updateUserRequest.getPassword());
            foundUser.setPassword(encodedPassword);
        }

        if (updateUserRequest.getRole() != null && !updateUserRequest.getRole().isBlank()) {
            try {
                Role newRole = Role.valueOf(updateUserRequest.getRole());
                foundUser.setRole(newRole);
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Ruolo non valido");
            }
        }

        // 3. Salva l’utente aggiornato
        User savedUser = userSvr.save(foundUser);

        // 4. Crea la risposta
        UserDTO userDTO = new UserDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
        // Se vuoi restituire un token aggiornato, potresti rigenerarlo.
        // Qui restituiamo solo i dati utente.
        LoginResponseDTO responseData = new LoginResponseDTO(userDTO, null);

        return new LoginResponseGeneric<>(APIStatus.SUCCESS, responseData, null);
    }

    // Elimina utente
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseGeneric<?> deleteUser(@PathVariable Long id) {
        // 1. Trova l'utente
        User foundUser = userSvr.findById(id);
        if (foundUser == null) {
            throw new BadRequestException("Utente non trovato");
        }

        // 2. Elimina l'utente
        userSvr.delete(foundUser);

        // 3. Restituisci conferma
        return new LoginResponseGeneric<>(APIStatus.SUCCESS, "Utente eliminato con successo", null);
    }




}