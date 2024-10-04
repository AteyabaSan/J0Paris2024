package com.joparis2024.controller;

import com.joparis2024.dto.CategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.joparis2024.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    // Créer une nouvelle catégorie
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            logger.info("Tentative de création d'une catégorie avec les données : {} - {}", categoryDTO.getName(), categoryDTO.getLocation());
            CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
            logger.info("Catégorie créée avec succès : ID = {}", createdCategory.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de la création de la catégorie : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création de la catégorie : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Récupérer toutes les catégories
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        logger.info("Récupération de toutes les catégories...");
        List<CategoryDTO> categories = categoryService.getAllCategories();
        logger.info("Nombre de catégories récupérées : {}", categories.size());
        return ResponseEntity.ok(categories);
    }

    // Récupérer une catégorie par son ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        logger.info("Tentative de récupération de la catégorie avec ID : {}", id);
        Optional<CategoryDTO> categoryDTO = categoryService.getCategoryById(id);
        if (categoryDTO.isPresent()) {
            logger.info("Catégorie trouvée : {}", categoryDTO.get().getName());
            return ResponseEntity.ok(categoryDTO.get());
        } else {
            logger.warn("Catégorie non trouvée pour l'ID : {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Mettre à jour une catégorie
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        try {
            logger.info("Tentative de mise à jour de la catégorie avec ID : {}", id);
            CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
            logger.info("Catégorie mise à jour avec succès : {}", updatedCategory.getName());
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la catégorie : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Supprimer une catégorie
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            logger.info("Tentative de suppression de la catégorie avec ID : {}", id);
            categoryService.deleteCategory(id);
            logger.info("Catégorie supprimée avec succès : ID = {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la catégorie : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}