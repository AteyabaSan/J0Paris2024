package com.joparis2024.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.joparis2024.model.Category;
import com.joparis2024.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    
    // Récupérer les événements par catégorie
	List<Event> findByCategory(Category category);
    
    // Récupérer un événement par son nom
    Optional<Event> findByEventName(String eventName);
}
