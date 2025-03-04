package com.nightlifeexplorer.beckend.service;

import com.nightlifeexplorer.beckend.entity.User;
import com.nightlifeexplorer.beckend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));

        // Verifica che il ruolo non sia null
        if (user.getRole() == null) {
            throw new IllegalStateException("Nessun ruolo assegnato all'utente: " + email);
        }

        // Converti il Role enum in GrantedAuthority
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())) // Usa .name() invece di .toString()
        );
    }
}