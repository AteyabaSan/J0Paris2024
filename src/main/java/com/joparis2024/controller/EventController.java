package com.joparis2024.controller;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.service.EventService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // Récupérer tous les événements (READ)
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // Créer un nouvel événement (CREATE)
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        try {
            EventDTO createdEvent = eventService.mapToDTO(eventService.createEvent(eventDTO));
            return ResponseEntity.ok(createdEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // Récupérer un événement par son nom (READ)
    @GetMapping("/name/{eventName}")
    public ResponseEntity<EventDTO> getEventByName(@PathVariable String eventName) {
        Optional<EventDTO> eventDTO = eventService.getEventByName(eventName);
        if (eventDTO.isPresent()) {
            return ResponseEntity.ok(eventDTO.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

 // Mettre à jour un événement par son nom
    @PutMapping("/update")
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO eventDTO) {
        try {
            // Utilise le nom de l'événement provenant du corps (eventDTO)
            EventDTO updatedEvent = eventService.updateEventByName(eventDTO.getEventName(), eventDTO);
            return ResponseEntity.ok(updatedEvent);  // Retourner le DTO mis à jour
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Si l'événement n'est pas trouvé
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // En cas d'autres erreurs
        }
    }


    // Supprimer un événement par son nom (DELETE)
    @DeleteMapping("/name/{eventName}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventName) throws Exception {
        eventService.deleteEvent(eventName);
        return ResponseEntity.noContent().build();
    }
}
