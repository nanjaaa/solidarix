package com.solidarix.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    private LocalDate birthday;

    // Quand la partie adresse sera bien implémentée, mettre optional à false
    @ManyToOne(optional = true)
    @JoinColumn(name = "address_id")
    private Location address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public static Role fromString(String roleStr) {
        try {
            return Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Invalid role: " + roleStr);
        }
    }

    // Méthode getAuthorities() pour retourner un seul rôle
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }



}
