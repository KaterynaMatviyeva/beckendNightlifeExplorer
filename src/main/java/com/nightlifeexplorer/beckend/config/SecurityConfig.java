package com.nightlifeexplorer.beckend.config;

import com.nightlifeexplorer.beckend.util.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig  {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }



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
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:4200")); // Dominio frontend
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true); // Permette cookie/token
            return config;
        }));
        http.csrf(csrf -> csrf.disable());
        http.formLogin(f -> f.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/register","/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/api/auth/**","/api/auth/me" ).permitAll()
                .requestMatchers("/api/auth/me").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/events").hasRole("ORGANIZER") // Solo ORGANIZER possono creare
                .requestMatchers(HttpMethod.GET, "/api/events/**").permitAll() // GET pubblico
                .anyRequest().authenticated()

        )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

//                .authorizeHttpRequests(auth -> auth
//                        // Consenti l'accesso a Swagger e agli endpoint di autenticazione
//                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/api/auth/**").permitAll()
//                        // Tutte le altre richieste richiedono autenticazione
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }




}