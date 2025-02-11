package com.nightlifeexplorer.beckend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password; // Memorizzata in forma cifrata

    @Enumerated(EnumType.STRING)
    private Role role; // ROLE_USER o ROLE_ORGANIZER
}
