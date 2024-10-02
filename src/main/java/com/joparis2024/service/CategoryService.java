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
        // Validation du DTO avant de créer la catégorie
        if (categoryDTO == null || categoryDTO.getName() == null || categoryDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la catégorie ne peut pas être vide");
        }

        // Mapper le DTO vers l'entité
        Category category = mapToEntity(categoryDTO);

        // Sauvegarder l'entité dans la base de données
        Category savedCategory = categoryRepository.save(category);

        // Mapper l'entité sauvegardée vers un DTO et le retourner
        return mapToDTO(savedCategory);
    }

    // Récupérer toutes les catégories
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        // Mapper chaque entité vers un DTO et retourner la liste
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for (Category category : categories) {
            categoryDTOs.add(mapToDTO(category));
        }

        return categoryDTOs;
    }

    // Récupérer une catégorie par ID
    public Optional<CategoryDTO> getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        // Mapper l'entité vers un DTO si elle existe
        return category.map(this::mapToDTO);
    }

    // Mettre à jour une catégorie
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) throws Exception {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (!existingCategory.isPresent()) {
            throw new Exception("Catégorie non trouvée");
        }

        // Validation des champs à mettre à jour
        if (categoryDTO.getName() == null || categoryDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la catégorie ne peut pas être vide.");
        }

        Category category = existingCategory.get();
        category.setName(categoryDTO.getName());
        category.setLocation(categoryDTO.getLocation());

        // Sauvegarder les modifications dans la base de données
        Category updatedCategory = categoryRepository.save(category);

        // Retourner le DTO mis à jour
        return mapToDTO(updatedCategory);
    }

    // Supprimer une catégorie
    public void deleteCategory(Long id) throws Exception {
        if (!categoryRepository.existsById(id)) {
            throw new Exception("Catégorie non trouvée");
        }

        // Supprimer la catégorie de la base de données
        categoryRepository.deleteById(id);
    }

    // Méthode pour mapper l'entité Category vers un DTO CategoryDTO
    public CategoryDTO mapToDTO(Category category) {
        if (category == null) {
            System.out.println("Impossible de mapper Category car il est nul");
            return null;
        }

        System.out.println("Mapping Category vers CategoryDTO: " + category);

        return new CategoryDTO(
            category.getId(),
            category.getName(),
            category.getLocation()
        );
    }

    // Méthode pour mapper un DTO CategoryDTO vers une entité Category
    public Category mapToEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            throw new IllegalArgumentException("CategoryDTO est nul, impossible de mapper vers Category.");
        }

        System.out.println("Mapping CategoryDTO vers Category : " + categoryDTO);

        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setLocation(categoryDTO.getLocation());

        return category;
    }
}

