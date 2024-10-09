package com.joparis2024.service;

import com.joparis2024.dto.CategoryDTO;
import com.joparis2024.events.CategoryCreatedEvent;
import com.joparis2024.events.CategoryDeletedEvent;
import com.joparis2024.events.CategoryUpdatedEvent;
import com.joparis2024.mapper.CategoryMapper;
import com.joparis2024.model.Category;
import com.joparis2024.model.Event;
import com.joparis2024.repository.CategoryRepository;
import com.joparis2024.repository.EventRepository;

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
    private EventRepository eventRepository;  // Injecter EventRepository pour gérer les associations

    @Autowired
    private ApplicationEventPublisher eventPublisher;  // Utilisation de Spring Events

    // Créer une catégorie
    public CategoryDTO createCategory(CategoryDTO categoryDTO) throws Exception {
        if (categoryDTO == null || categoryDTO.getName() == null || categoryDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la catégorie ne peut pas être vide");
        }

        Category category = categoryMapper.toEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);

        eventPublisher.publishEvent(new CategoryCreatedEvent(this, categoryMapper.toDTO(savedCategory)));
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

        Category updatedCategory = categoryRepository.save(category);
        eventPublisher.publishEvent(new CategoryUpdatedEvent(this, categoryMapper.toDTO(updatedCategory)));

        return categoryMapper.toDTO(updatedCategory);
    }

    // Supprimer une catégorie
    public void deleteCategory(Long id) throws Exception {
        if (!categoryRepository.existsById(id)) {
            throw new Exception("Catégorie non trouvée");
        }

        categoryRepository.deleteById(id);
        eventPublisher.publishEvent(new CategoryDeletedEvent(this, id));
    }

    // Méthode pour associer une catégorie à des événements
    public void assignCategoryToEvents(CategoryDTO categoryDTO) throws Exception {
        if (categoryDTO.getId() == null) {
            throw new IllegalArgumentException("L'ID de la catégorie est requis.");
        }

        Optional<Category> category = categoryRepository.findById(categoryDTO.getId());
        if (!category.isPresent()) {
            throw new Exception("Catégorie non trouvée.");
        }

        List<Event> events = eventRepository.findAll();  // Ou récupérer une liste d'événements spécifique
        for (Event event : events) {
            event.setCategory(category.get());  // Associer la catégorie à chaque événement
            eventRepository.save(event);
        }
    }

    // Méthode pour dissocier une catégorie des événements
    public void removeCategoryFromEvents(Long categoryId) throws Exception {
        if (categoryId == null) {
            throw new IllegalArgumentException("L'ID de la catégorie est requis.");
        }

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new Exception("Catégorie non trouvée.");
        }

        List<Event> events = eventRepository.findByCategory(category.get());
        for (Event event : events) {
            event.setCategory(null);  // Dissocier la catégorie de chaque événement
            eventRepository.save(event);
        }
    }
}
