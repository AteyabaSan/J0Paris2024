package com.joparis2024.service;

import com.joparis2024.dto.CategoryDTO;
import com.joparis2024.events.CategoryCreatedEvent;
import com.joparis2024.events.CategoryDeletedEvent;
import com.joparis2024.events.CategoryUpdatedEvent;
import com.joparis2024.mapper.CategoryMapper;
import com.joparis2024.model.Category;
import com.joparis2024.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;  // Utilisation de Spring Events

    // Créer une catégorie
    public CategoryDTO createCategory(CategoryDTO categoryDTO) throws Exception {
        // Validation du DTO avant de créer la catégorie
        if (categoryDTO == null || categoryDTO.getName() == null || categoryDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la catégorie ne peut pas être vide");
        }

        // Mapper le DTO vers l'entité via le mapper
        Category category = categoryMapper.toEntity(categoryDTO);

        // Sauvegarder l'entité dans la base de données
        Category savedCategory = categoryRepository.save(category);

        // Publier l'événement CategoryCreatedEvent
        eventPublisher.publishEvent(new CategoryCreatedEvent(this, categoryMapper.toDTO(savedCategory)));

        // Retourner le DTO
        return categoryMapper.toDTO(savedCategory);
    }

    // Récupérer toutes les catégories
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for (Category category : categories) {
            categoryDTOs.add(categoryMapper.toDTO(category));
        }
        return categoryDTOs;
    }

    // Récupérer une catégorie par ID
    public Optional<CategoryDTO> getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(categoryMapper::toDTO);
    }

    // Mettre à jour une catégorie
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) throws Exception {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (!existingCategory.isPresent()) {
            throw new Exception("Catégorie non trouvée");
        }

        if (categoryDTO.getName() == null || categoryDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la catégorie ne peut pas être vide.");
        }

        Category category = existingCategory.get();
        category.setName(categoryDTO.getName());
        category.setLocation(categoryDTO.getLocation());

        // Sauvegarder les modifications dans la base de données
        Category updatedCategory = categoryRepository.save(category);

        // Publier l'événement CategoryUpdatedEvent
        eventPublisher.publishEvent(new CategoryUpdatedEvent(this, categoryMapper.toDTO(updatedCategory)));

        return categoryMapper.toDTO(updatedCategory);
    }

    // Supprimer une catégorie
    public void deleteCategory(Long id) throws Exception {
        if (!categoryRepository.existsById(id)) {
            throw new Exception("Catégorie non trouvée");
        }

        // Supprimer la catégorie de la base de données
        categoryRepository.deleteById(id);

        // Publier l'événement CategoryDeletedEvent
        eventPublisher.publishEvent(new CategoryDeletedEvent(this, id));
    }
}
