package com.joparis2024.dto;

import java.util.List;

public class UserDTO {
	
	private Long id;
    private String username;
    private String email;
    private List<String> roles;  // Liste de rôles
    private Boolean enabled;
    private String phoneNumber;
    private String password;

    // Constructeurs
    public UserDTO() {
    }

    // Constructeur avec l'ID et les rôles
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
        return roles;  // Liste de rôles
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;  // Liste de rôles
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
    
    // Getters et Setters pour l'ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    // Getters et Setters pour le mot de passe
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


