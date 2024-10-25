package com.joparis2024.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private List<String> roles;  // Liste de rôles
    private Boolean enabled = true;
    private String phoneNumber;
    private String password;

    // Constructeurs
    public UserDTO() {}

    public UserDTO(Long id, String username, String email, List<String> roles, Boolean enabled, String phoneNumber, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;  // Liste de rôles
        this.enabled = enabled;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        if (roles == null) {
            roles = new ArrayList<>(); // Initialiser la liste si elle est null
        }
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Méthode pour obtenir les autorités (rôles) de l'utilisateur
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}