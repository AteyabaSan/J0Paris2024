package com.joparis2024.mapper;

import org.springframework.stereotype.Component;
import com.joparis2024.dto.CategoryDTO;
import com.joparis2024.model.Category;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setLocation(category.getLocation());

        return categoryDTO;
    }

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
}
