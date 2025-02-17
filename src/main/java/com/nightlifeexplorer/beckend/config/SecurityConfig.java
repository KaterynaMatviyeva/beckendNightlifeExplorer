package com.nightlifeexplorer.beckend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    //Swagger senza auth
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.formLogin(f -> f.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        http.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll());

//                .authorizeHttpRequests(auth -> auth
//                        // Consenti l'accesso a Swagger e agli endpoint di autenticazione
//                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/api/auth/**").permitAll()
//                        // Tutte le altre richieste richiedono autenticazione
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults());
        return http.build();
    }



}