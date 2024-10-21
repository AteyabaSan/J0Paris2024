package com.joparis2024.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    // Ajout de méthodes spécifiques si nécessaire, par exemple, pour rechercher une offre par nom
    Offer findByName(String name);
    
    List<Offer> findByEventsId(Long eventId);

}

