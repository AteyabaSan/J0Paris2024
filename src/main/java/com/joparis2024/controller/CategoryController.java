package com.joparis2024.controller;

import com.joparis2024.dto.CategoryDTO;
import com.joparis2024.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

   
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Créer une nouvelle catégorie
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        logger.info("Requête reçue pour créer une nouvelle catégorie : {}", categoryDTO);
        try {
            CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
            logger.info("Catégorie créée avec succès : {}", createdCategory);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la catégorie : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer toutes les catégories
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        logger.info("Requête reçue pour récupérer toutes les catégories");
        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            logger.info("Catégories récupérées avec succès, nombre de catégories : {}", categories.size());
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des catégories : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer une catégorie par ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        logger.info("Requête reçue pour récupérer la catégorie avec l'ID : {}", id);
        Optional<CategoryDTO> categoryDTO = categoryService.getCategoryById(id);
        if (categoryDTO.isPresent()) {
            logger.info("Catégorie trouvée avec succès : {}", categoryDTO.get());
            return new ResponseEntity<>(categoryDTO.get(), HttpStatus.OK);
        } else {
            logger.warn("Catégorie non trouvée pour l'ID : {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Mettre à jour une catégorie
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        logger.info("Requête reçue pour mettre à jour la catégorie avec l'ID : {}", id);
        try {
            CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
            logger.info("Catégorie mise à jour avec succès : {}", updatedCategory);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la catégorie : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Supprimer une catégorie
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        logger.info("Requête reçue pour supprimer la catégorie avec l'ID : {}", id);
        try {
            categoryService.deleteCategory(id);
            logger.info("Catégorie supprimée avec succès, ID : {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la catégorie : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
