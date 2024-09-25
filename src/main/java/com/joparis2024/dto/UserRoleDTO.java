package com.joparis2024.dto;

import com.joparis2024.model.User;
import com.joparis2024.model.Role;

public class UserRoleDTO {
    
    private Long id;
    private User user;
    private Role role;

    // Constructeurs
    public UserRoleDTO() {}

    public UserRoleDTO(Long id, User user, Role role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
