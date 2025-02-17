package com.nightlifeexplorer.beckend.entity;

import com.nightlifeexplorer.beckend.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@Data
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // ROLE_USER o ROLE_ORGANIZER
    @ManyToMany
    @JoinTable(
            name = "user_saved_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> savedEvents = new ArrayList<>();

    public User(String email, String username, String password, Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
