package com.joparis2024.controller;

import com.joparis2024.dto.CategoryDTO;
import com.joparis2024.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//
//import java.util.Arrays;
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
    @GetMapping("/json")
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

    // Associer une catégorie à un ou plusieurs événements
    @PostMapping("/{categoryId}/events")
    public ResponseEntity<Void> assignCategoryToEvents(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {
        logger.info("Requête reçue pour associer une catégorie aux événements pour la catégorie ID: {}", categoryId);
        try {
            categoryDTO.setId(categoryId); // Associer l'ID de la catégorie
            categoryService.assignCategoryToEvents(categoryDTO); // Appel du service pour associer la catégorie
            logger.info("Catégorie associée aux événements avec succès.");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erreur lors de l'association de la catégorie aux événements : {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
//    @GetMapping("view")
//    public String getCategories(Model model) {
//        // Ajouter des informations de catégories au modèle
//        // Ce serait mieux de récupérer les catégories depuis une base de données, 
//        // mais ici je vais simplement simuler avec une liste statique pour l'exemple.
//        
//        List<CategoryDTO> categories = Arrays.asList(
//            new CategoryDTO(1L, "Athlétisme", "Stade De France - Saint-Denis"),
//            new CategoryDTO(3L, "Aviron", "Vaires-sur-Marne - Seine et Marne"),
//            new CategoryDTO(4L, "Natation", "Centre Aquatique - Saint-Denis"),
//            new CategoryDTO(5L, "Escrime", "Grand Palais - Paris Centre"),
//            new CategoryDTO(6L, "Gymnastique", "Bercy Arena - Accor Arena"),
//            new CategoryDTO(7L, "Boxe", "Porte de la Chapelle Arena"),
//            new CategoryDTO(8L, "Basketball", "Porte de la Chapelle Arena"),
//            new CategoryDTO(9L, "Football", "Parc des Princes"),
//            new CategoryDTO(10L, "Rugby", "Stade de France"),
//            new CategoryDTO(11L, "Cyclisme", "Champs Elysées - Colline d'Elancourt"),
//            new CategoryDTO(12L, "Tennis", "Rolland-Garros"),
//            new CategoryDTO(13L, "Volleyball", "Parc des expositions Versailles / Champs de Mars-Tour Eiffel")
//        );
//
//        model.addAttribute("categories", categories);
//        return "categories"; // Retourne la vue Thymeleaf
//    }
}
