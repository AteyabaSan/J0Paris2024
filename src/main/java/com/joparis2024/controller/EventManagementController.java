package com.joparis2024.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.OfferDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.OfferMapper;
import com.joparis2024.mapper.TicketMapper;
import com.joparis2024.model.Offer;
import com.joparis2024.model.Ticket;
import com.joparis2024.service.EventManagementFacade;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/event-management")
public class EventManagementController {

    private static final Logger logger = LoggerFactory.getLogger(EventManagementController.class);

    @Autowired
    private EventManagementFacade eventManagementFacade;
    
    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private TicketMapper ticketMapper;

    // Assigner des tickets à un événement
    @PostMapping("/events/{eventId}/assign-tickets")
    public ResponseEntity<String> assignTicketsToEvent(@PathVariable Long eventId, @RequestBody List<TicketDTO> ticketDTOs) {
        List<Long> ticketIds = new ArrayList<>();
        for (TicketDTO ticketDTO : ticketDTOs) {
            ticketIds.add(ticketDTO.getId());
        }

        logger.info("Assignation des tickets {} à l'événement {}", ticketIds, eventId);
        try {
            eventManagementFacade.assignTicketsToEvent(eventId, ticketIds);
            return ResponseEntity.ok("Assignation réussie des tickets à l'événement " + eventId);
        } catch (Exception e) {
            logger.error("Erreur lors de l'assignation des tickets {} à l'événement {}: {}", ticketIds, eventId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'assignation des tickets.");
        }
    }

    // Récupérer les tickets associés à un événement
    @GetMapping("/events/{eventId}/tickets")
    public ResponseEntity<List<TicketDTO>> getTicketsForEvent(@PathVariable Long eventId) {
        logger.info("Récupération des tickets pour l'événement {}", eventId);
        try {
            List<Ticket> tickets = eventManagementFacade.getTicketsForEvent(eventId);
            List<TicketDTO> ticketDTOs = ticketMapper.toDTOs(tickets);
            return ResponseEntity.ok(ticketDTOs);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des tickets pour l'événement {}: {}", eventId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Assigner des offres à un événement
    @PostMapping("/events/{eventId}/assign-offers")
    public ResponseEntity<String> assignOffersToEvent(@PathVariable Long eventId, @RequestBody List<OfferDTO> offerDTOs) {
        List<Long> offerIds = new ArrayList<>();
        for (OfferDTO offerDTO : offerDTOs) {
            offerIds.add(offerDTO.getId());
        }

        logger.info("Assignation des offres {} à l'événement {}", offerIds, eventId);
        try {
            eventManagementFacade.assignOffersToEvent(eventId, offerIds);
            return ResponseEntity.ok("Assignation réussie des offres à l'événement " + eventId);
        } catch (Exception e) {
            logger.error("Erreur lors de l'assignation des offres {} à l'événement {}: {}", offerIds, eventId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'assignation des offres.");
        }
    }
    
    // Assigner des événements à une offre (manquante)
    @PostMapping("/offers/{offerId}/assign-events")
    public ResponseEntity<String> assignEventsToOffer(@PathVariable Long offerId, @RequestBody List<EventDTO> eventDTOs) {
        logger.info("Assignation des événements à l'offre {}", offerId);
        try {
            List<Long> eventIds = new ArrayList<>();
            
            // Récupération manuelle des IDs des événements
            for (EventDTO eventDTO : eventDTOs) {
                eventIds.add(eventDTO.getId());
            }
            
            // Appel au service pour assigner les événements
            eventManagementFacade.assignEventsToOffer(offerId, eventIds);
            
            return ResponseEntity.ok("Assignation réussie des événements à l'offre " + offerId);
        } catch (Exception e) {
            logger.error("Erreur lors de l'assignation des événements à l'offre {}: {}", offerId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'assignation des événements.");
        }
    }

    // Récupérer les offres associées à un événement
    @GetMapping("/events/{eventId}/offers")
    public ResponseEntity<List<OfferDTO>> getOffersForEvent(@PathVariable Long eventId) {
        logger.info("Récupération des offres pour l'événement {}", eventId);
        try {
            List<Offer> offers = eventManagementFacade.getOffersForEvent(eventId);
            List<OfferDTO> offerDTOs = offerMapper.toDTOs(offers);
            return ResponseEntity.ok(offerDTOs);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des offres pour l'événement {}: {}", eventId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Supprimer une association d'offre avec un événement
    @DeleteMapping("/events/{eventId}/offers/{offerId}")
    public ResponseEntity<String> deleteEventOffer(@PathVariable Long eventId, @PathVariable Long offerId) {
        logger.info("Suppression de l'association entre l'offre {} et l'événement {}", offerId, eventId);
        try {
            eventManagementFacade.deleteEventOffer(eventId, offerId);
            return ResponseEntity.ok("Association supprimée entre l'offre et l'événement.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Association non trouvée.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression de l'association.");
        }
    }

    // Supprimer un ticket associé à un événement
    @DeleteMapping("/events/{eventId}/tickets/{ticketId}")
    public ResponseEntity<Void> removeTicketFromEvent(@PathVariable Long eventId, @PathVariable Long ticketId) {
        logger.info("Suppression du ticket {} de l'événement {}", ticketId, eventId);
        try {
            eventManagementFacade.removeTicketFromEvent(eventId, ticketId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
