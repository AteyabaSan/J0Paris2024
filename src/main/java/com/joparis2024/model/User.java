package com.joparis2024.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ajout de l'ID
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    // Constructeur avec uniquement les attributs nécessaires
    public User(String username, String email, String role, Boolean enabled, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
        this.phoneNumber = phoneNumber;
    }
}
