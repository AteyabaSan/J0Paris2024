package com.joparis2024.dto;

public class UserDTO {

    private String username;
    private String email;
    private String role;
    private Boolean enabled;
    private String phoneNumber;

    // Constructeurs
    public UserDTO() {
    }

    public UserDTO(String username, String email, String role, Boolean enabled, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
        this.phoneNumber = phoneNumber;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
}

