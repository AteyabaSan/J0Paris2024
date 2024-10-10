package com.joparis2024.dto;


public class UserRoleDTO {
    
    private Long id;
    private Long userId;  // On utilise l'ID au lieu de l'entité complète
    private Long roleId;  // Idem pour le rôle

    // Constructeurs
    public UserRoleDTO() {}

    public UserRoleDTO(Long id, Long userId, Long roleId) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
