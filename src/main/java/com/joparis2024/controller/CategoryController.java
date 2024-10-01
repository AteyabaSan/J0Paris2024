package com.joparis2024.controller;

import com.joparis2024.dto.CategoryDTO;
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

    @Autowired
    private CategoryService categoryService;

    // Créer une nouvelle catégorie
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            System.out.println("Tentative de création d'une catégorie avec les données : " + categoryDTO.getName() + " - " + categoryDTO.getLocation());
            CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
            System.out.println("Catégorie créée avec succès : ID = " + createdCategory.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur lors de la création de la catégorie : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            System.out.println("Erreur inattendue lors de la création de la catégorie : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Récupérer toutes les catégories
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        System.out.println("Récupération de toutes les catégories...");
        List<CategoryDTO> categories = categoryService.getAllCategories();
        System.out.println("Nombre de catégories récupérées : " + categories.size());
        return ResponseEntity.ok(categories);
    }

    // Récupérer une catégorie par son ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        System.out.println("Tentative de récupération de la catégorie avec ID : " + id);
        Optional<CategoryDTO> categoryDTO = categoryService.getCategoryById(id);
        if (categoryDTO.isPresent()) {
            System.out.println("Catégorie trouvée : " + categoryDTO.get().getName());
            return ResponseEntity.ok(categoryDTO.get());
        } else {
            System.out.println("Catégorie non trouvée pour l'ID : " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Mettre à jour une catégorie
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        try {
            System.out.println("Tentative de mise à jour de la catégorie avec ID : " + id);
            CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
            System.out.println("Catégorie mise à jour avec succès : " + updatedCategory.getName());
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour de la catégorie : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Supprimer une catégorie
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            System.out.println("Tentative de suppression de la catégorie avec ID : " + id);
            categoryService.deleteCategory(id);
            System.out.println("Catégorie supprimée avec succès : ID = " + id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la catégorie : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
