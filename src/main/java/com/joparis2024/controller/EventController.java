package com.joparis2024.controller;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.service.EventService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // Créer un nouvel événement (CREATE)
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        try {
            // Appel du service avec le DTO directement
            EventDTO createdEventDTO = eventService.createEvent(eventDTO);
            return ResponseEntity.ok(createdEventDTO);
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
    public ResponseEntity<?> updateEvent(@RequestBody EventDTO eventDTO) {
        try {
            // Log pour voir les données reçues
            System.out.println("Mise à jour de l'événement: " + eventDTO.getEventName());

            // Appeler le service pour mettre à jour l'événement par nom
            EventDTO updatedEvent = eventService.updateEventByName(eventDTO.getEventName(), eventDTO);

            // Si l'événement est bien mis à jour, retourner un code 200 OK avec les détails
            return ResponseEntity.ok(updatedEvent);
        } catch (EntityNotFoundException e) {
            // Si l'événement n'est pas trouvé, retourner un 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement non trouvé: " + eventDTO.getEventName());
        } catch (Exception e) {
            // Log de l'erreur pour faciliter le débogage
            System.err.println("Erreur lors de la mise à jour de l'événement: " + e.getMessage());

            // Retourner un code 500 en cas d'erreur interne
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne lors de la mise à jour de l'événement.");
        }
    }

    // Supprimer un événement par son nom (DELETE)
    @DeleteMapping("/name/{eventName}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventName) throws Exception {
        eventService.deleteEvent(eventName);
        return ResponseEntity.noContent().build();
    }
}
