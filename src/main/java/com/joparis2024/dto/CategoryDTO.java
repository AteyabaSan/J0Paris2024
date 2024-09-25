package com.joparis2024.dto;

public class CategoryDTO {
    
    private Long id;  // Identifiant de la catégorie
    private String name;  // Nom de la catégorie
    private String location;  // Localisation de la catégorie

    // Constructeurs
    public CategoryDTO() {}

    public CategoryDTO(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

