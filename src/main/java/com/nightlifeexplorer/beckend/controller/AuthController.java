package com.nightlifeexplorer.beckend.controller;

import com.nightlifeexplorer.beckend.dto.LoginRequest;
import com.nightlifeexplorer.beckend.dto.LoginResponse;

import com.nightlifeexplorer.beckend.dto.RegisterRequest;
import com.nightlifeexplorer.beckend.dto.TokenDTO;
import com.nightlifeexplorer.beckend.entity.User;
import com.nightlifeexplorer.beckend.enums.APIStatus;
import com.nightlifeexplorer.beckend.enums.Role;
import com.nightlifeexplorer.beckend.exception.BadRequestException;


import com.nightlifeexplorer.beckend.service.UserService;
import com.nightlifeexplorer.beckend.util.JwtUtil;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse<TokenDTO> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult validation) {
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

        // Genera un token JWT per il nuovo utente
        String token = jwt.createToken(registerRequest.getEmail());

        return new LoginResponse<TokenDTO>(APIStatus.SUCCESS, new TokenDTO(token), null);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public void getMe(){

    }
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse<TokenDTO> login(@RequestBody LoginRequest credentials, BindingResult validation){
        if(validation.hasErrors()) throw new BadRequestException(
                validation.getAllErrors()
                        .stream()
                        .map(err->err.getDefaultMessage()).toString());
        User found = this.userSvr.findByEmail(credentials.email());
        if (passwordEncoder.matches(credentials.password(), found.getPassword())) {
                    return new LoginResponse<TokenDTO>(APIStatus.SUCCESS, new TokenDTO(jwt.createToken(credentials.email())) , null);

                }else throw new BadRequestException("Wrong password");
    }



}