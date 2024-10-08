package com.joparis2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joparis2024.dto.CategoryDTO;
import com.joparis2024.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InteractionFacade {

    @Autowired
    private EventManagementFacade eventManagementFacade;

    @Autowired
    private CategoryService categoryService;

    private static final Logger logger = LoggerFactory.getLogger(InteractionFacade.class);

    // Gestion de la création d'une catégorie et de ses interactions avec les événements
    public void handleCategoryCreation(Category category, CategoryDTO categoryDTO) throws Exception {
        if (categoryDTO.getEventIds() != null && !categoryDTO.getEventIds().isEmpty()) {
            logger.info("Assignation des événements à la nouvelle catégorie ID: {}", category.getId());
            eventManagementFacade.assignCategoryToEvents(category.getId(), categoryDTO.getEventIds(), categoryService);
        }
    }

    // Gestion de la mise à jour d'une catégorie et de ses interactions avec les événements
    public void handleCategoryUpdate(Category category, CategoryDTO categoryDTO) throws Exception {
        if (categoryDTO.getEventIds() != null && !categoryDTO.getEventIds().isEmpty()) {
            logger.info("Mise à jour des événements liés à la catégorie ID: {}", category.getId());
            eventManagementFacade.assignCategoryToEvents(category.getId(), categoryDTO.getEventIds(), categoryService);
        }
    }

    // Gestion de la suppression d'une catégorie
    public void handleCategoryDeletion(Long categoryId) {
        logger.info("Suppression des interactions liées à la catégorie ID: {}", categoryId);
        // Ici, vous pouvez ajouter la logique pour gérer la suppression des interactions
        // si nécessaire, ou toute autre action liée à la suppression d'une catégorie.
    }
}
