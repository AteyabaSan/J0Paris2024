package com.joparis2024.controller;

import com.joparis2024.dto.EventDTO;
//import com.joparis2024.model.Event;
import com.joparis2024.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Récupérer tous les événements
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        logger.info("Récupération de tous les événements");
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // Récupérer un événement par ID
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        logger.info("Récupération de l'événement avec ID: {}", id);
        try {
            EventDTO eventDTO = eventService.getEventById(id);
            return ResponseEntity.ok(eventDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'événement avec ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    // Créer un événement
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        logger.info("Création d'un nouvel événement: {}", eventDTO.getEventName());
        try {
            // Appel au service pour créer l'événement
            EventDTO createdEventDTO = eventService.createEvent(eventDTO);

            // Retourner le DTO avec l'ID généré
            return ResponseEntity.status(201).body(createdEventDTO);
        } catch (IllegalArgumentException e) {
            // Gestion des erreurs de validation
            logger.error("Erreur de validation lors de la création de l'événement: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            // Gestion des autres erreurs
            logger.error("Erreur lors de la création de l'événement", e);
            return ResponseEntity.badRequest().build();
        }
    }



//    // Méthode pour convertir l'entité Event en EventDTO
//    private EventDTO convertToDTO(Event event) {
//        EventDTO eventDTO = new EventDTO();
//        eventDTO.setEventName(event.getEventName());
//        eventDTO.setDescription(event.getDescription());
//        eventDTO.setEventDate(event.getEventDate());
//        eventDTO.setCategoryId(event.getCategory().getId());
//        return eventDTO;
//    }


    // Mettre à jour un événement
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        logger.info("Mise à jour de l'événement avec ID: {}", id);
        try {
            EventDTO updatedEvent = eventService.updateEvent(id, eventDTO);
            return ResponseEntity.ok(updatedEvent);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'événement avec ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un événement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        logger.info("Suppression de l'événement avec ID: {}", id);
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'événement avec ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/category/{categoryId}")
    public String getEventsByCategory(@PathVariable Long categoryId, Model model) {
        List<EventDTO> events = eventService.getEventsByCategory(categoryId);
        model.addAttribute("events", events);
        return "events"; // La page qui liste les événements
    }
}
