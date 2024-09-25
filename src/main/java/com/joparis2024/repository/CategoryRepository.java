package com.joparis2024.repository;

import com.joparis2024.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Trouver une catégorie par nom
    Optional<Category> findByName(String name);

    // Trouver une catégorie par lieu (location)
    List<Category> findByLocation(String location);
}
