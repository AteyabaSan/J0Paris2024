package com.joparis2024.service;

import com.joparis2024.dto.CategoryDTO;
import com.joparis2024.model.Category;
import com.joparis2024.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Créer une catégorie
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
    	Category category = mapToEntity(categoryDTO);


        Category savedCategory = categoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    // Récupérer toutes les catégories
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for (Category category : categories) {
            categoryDTOs.add(mapToDTO(category));
        }
        return categoryDTOs;
    }

    // Récupérer une catégorie par ID
    public Optional<CategoryDTO> getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(this::mapToDTO);
    }

    // Mettre à jour une catégorie
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) throws Exception {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (!existingCategory.isPresent()) {
            throw new Exception("Catégorie non trouvée");
        }

        Category category = existingCategory.get();
        category.setName(categoryDTO.getName());
        category.setLocation(categoryDTO.getLocation());

        Category updatedCategory = categoryRepository.save(category);
        return mapToDTO(updatedCategory);
    }

    // Supprimer une catégorie
    public void deleteCategory(Long id) throws Exception {
        if (!categoryRepository.existsById(id)) {
            throw new Exception("Catégorie non trouvée");
        }
        categoryRepository.deleteById(id);
    }

    // Mapper l'entité Category vers un DTO CategoryDTO
    private CategoryDTO mapToDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getLocation());
    }

    // Mapper le DTO CategoryDTO vers l'entité Category
    private Category mapToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setLocation(categoryDTO.getLocation());
        return category;
    }
}
