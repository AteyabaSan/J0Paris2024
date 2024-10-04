package com.joparis2024.mapper;

import org.springframework.stereotype.Component;
import com.joparis2024.dto.CategoryDTO;
import com.joparis2024.model.Category;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {

    // Convertir une entité Category en CategoryDTO
    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }

        return new CategoryDTO(
            category.getId(),
            category.getName(),
            category.getLocation()
        );
    }

    // Convertir un DTO CategoryDTO en entité Category
    public Category toEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }

        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setLocation(categoryDTO.getLocation());

        return category;
    }

    // Convertir une liste de catégories en DTOs
    public List<CategoryDTO> toDTOs(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return new ArrayList<>();
        }

        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les entités en DTOs
        for (Category category : categories) {
            categoryDTOs.add(toDTO(category));
        }

        return categoryDTOs;
    }

    // Convertir une liste de DTOs en entités
    public List<Category> toEntities(List<CategoryDTO> categoryDTOs) {
        if (categoryDTOs == null || categoryDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Category> categories = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les DTOs en entités
        for (CategoryDTO categoryDTO : categoryDTOs) {
            categories.add(toEntity(categoryDTO));
        }

        return categories;
    }
}
