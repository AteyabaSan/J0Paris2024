package com.joparis2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.TicketMapper;

import com.joparis2024.model.Event;
import com.joparis2024.model.EventOffer;
import com.joparis2024.model.Offer;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.EventOfferRepository;
import com.joparis2024.repository.EventRepository;
import com.joparis2024.repository.TicketRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventManagementFacade {

    @Autowired
    private EventService eventService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private EventOfferRepository eventOfferRepository;

    private static final Logger logger = LoggerFactory.getLogger(EventManagementFacade.class);
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private  TicketRepository ticketRepository;
    
    // Méthode complexe : assigner des tickets à un événement
    @Transactional
    public void assignTicketsToEvent(Long eventId, List<Long> ticketIds) throws Exception {
        logger.info("Association des tickets à l'événement ID: {}", eventId);
        EventDTO eventDTO = eventService.getEventById(eventId);  // Récupération de l'événement via EventService
        Event event = eventService.toEntity(eventDTO);  // Conversion en entité

        List<Ticket> tickets = new ArrayList<>();
        for (Long ticketId : ticketIds) {
            TicketDTO ticketDTO = ticketService.getTicketById(ticketId);  // Récupération du TicketDTO
            Ticket ticket = ticketMapper.toEntity(ticketDTO);  // Conversion de TicketDTO en Ticket
            ticket.setEvent(event);  // Associer l'événement au ticket
            tickets.add(ticket);
            // Enregistrer directement chaque ticket après l'association
            ticketRepository.save(ticket);
        }

        logger.info("Assignation des tickets réussie pour l'événement {}", eventId);
    }
    
    @Transactional(readOnly = true)
    public List<Ticket> getTicketsForEvent(Long eventId) throws Exception {
        logger.info("Récupération des tickets associés à l'événement ID: {}", eventId);

        // Vérifier si l'événement existe sans assigner à une variable
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("L'événement avec ID " + eventId + " n'existe pas");
        }

        // Utiliser la nouvelle méthode qui fait un JOIN FETCH pour charger les tickets avec leurs événements associés
        List<Ticket> tickets = ticketRepository.findTicketsWithEvent(eventId);

        if (tickets.isEmpty()) {
            logger.info("Aucun ticket trouvé pour l'événement ID: {}", eventId);
        } else {
            logger.info("Tickets trouvés : {}", tickets);
        }

        return tickets;
    }


    // Méthode complexe : assigner des offres à un événement
    @Transactional
    public void assignOffersToEvent(Long eventId, List<Long> offerIds) throws Exception {
        logger.info("Association des offres à l'événement ID: {}", eventId);
        
        EventDTO eventDTO = eventService.getEventById(eventId);
        if (eventDTO == null) {
            throw new EntityNotFoundException("Événement non trouvé avec l'ID: " + eventId);
        }
        
        Event event = eventService.toEntity(eventDTO);

        for (Long offerId : offerIds) {
            Offer offer = offerService.findById(offerId);
            if (offer == null) {
                throw new EntityNotFoundException("Offre non trouvée avec l'ID: " + offerId);
            }

            // Vérification si l'association Event-Offer existe déjà
            Optional<EventOffer> existingRelation = eventOfferRepository.findByEventIdAndOfferId(event.getId(), offer.getId());
            if (existingRelation.isPresent()) {
                logger.info("L'association entre l'événement {} et l'offre {} existe déjà", eventId, offerId);
                continue;  // Ne pas insérer de doublons
            }

            // Création de la relation EventOffer et sauvegarde
            EventOffer eventOffer = new EventOffer();
            eventOffer.setEvent(event);
            eventOffer.setOffer(offer);
            eventOfferRepository.save(eventOffer);  // Sauvegarde de l'association
        }

        logger.info("Assignation réussie des offres {} à l'événement {}", offerIds, eventId);
    }



    // Méthode complexe : assigner des événements à une offre
    @Transactional
    public void assignEventsToOffer(Long offerId, List<Long> eventIds) throws Exception {
        Offer offer = offerService.findById(offerId);
        
        for (Long eventId : eventIds) {
            // Récupération de l'EventDTO via l'ID
            EventDTO eventDTO = eventService.getEventById(eventId);
            
            // Conversion de EventDTO en Event
            Event event = eventService.toEntity(eventDTO);
            
            // Création de la relation entre l'événement et l'offre
            EventOffer eventOffer = new EventOffer();
            eventOffer.setEvent(event);
            eventOffer.setOffer(offer);
            eventOfferRepository.save(eventOffer);
        }
    }


    // Méthode complexe : récupérer les offres associées à un événement
    @Transactional(readOnly = true)
    public List<Offer> getOffersForEvent(Long eventId) throws Exception {
        logger.info("Récupération des offres pour l'événement ID: {}", eventId);
        EventDTO eventDTO = eventService.getEventById(eventId);  // Récupération de l'événement via EventService
        Event event = eventService.toEntity(eventDTO);  // Conversion en entité

        List<EventOffer> eventOffers = eventOfferRepository.findByEvent(event);  // Récupération des EventOffer
        List<Offer> offers = new ArrayList<>();

        for (EventOffer eventOffer : eventOffers) {
            offers.add(eventOffer.getOffer());
        }

        return offers;
    }


    // Méthode pour supprimer une association d'offre avec un événement
    @Transactional
    public void deleteEventOffer(Long eventId, Long offerId) throws Exception {
        logger.info("Suppression de l'association entre l'événement ID: {} et l'offre ID: {}", eventId, offerId);
        
        // Récupérer l'association EventOffer et vérifier qu'elle existe
        EventOffer eventOffer = eventOfferRepository.findByEventIdAndOfferId(eventId, offerId)
                .orElseThrow(() -> new EntityNotFoundException("Association non trouvée entre l'événement et l'offre"));

        // Suppression de l'association dans la base de données
        eventOfferRepository.delete(eventOffer);
        
        logger.info("Association supprimée avec succès entre l'événement ID: {} et l'offre ID: {}", eventId, offerId);
    }
    
    @Transactional
    public void removeTicketFromEvent(Long eventId, Long ticketId) throws Exception {
        logger.info("Suppression du ticket ID: {} de l'événement ID: {}", ticketId, eventId);

        // Récupérer le ticket via le service et vérifier son association avec l'événement
        TicketDTO ticketDTO = ticketService.getTicketById(ticketId);
        if (ticketDTO.getEvent() == null || !ticketDTO.getEvent().getId().equals(eventId)) {
            throw new Exception("Le ticket ID: " + ticketId + " n'est pas associé à l'événement ID: " + eventId);
        }

        // Dissocier le ticket de l'événement
        ticketDTO.setEvent(null);

        // Sauvegarder la mise à jour via le service
        ticketService.updateTicket(ticketId, ticketDTO);

        logger.info("Ticket ID: {} dissocié avec succès de l'événement ID: {}", ticketId, eventId);
    }
}
