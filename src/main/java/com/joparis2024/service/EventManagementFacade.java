package com.joparis2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.TicketMapper;
import com.joparis2024.dto.CategoryDTO;

import com.joparis2024.model.Event;
import com.joparis2024.model.EventOffer;
import com.joparis2024.model.Offer;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.EventOfferRepository;


import jakarta.persistence.EntityNotFoundException;

@Service
public class EventManagementFacade {

    @Autowired
    private EventService eventService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private EventOfferRepository eventOfferRepository;

    private static final Logger logger = LoggerFactory.getLogger(EventManagementFacade.class);

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
        }

        // Mise à jour des tickets via le TicketService pour sauvegarder l'association
        for (Ticket ticket : tickets) {
            ticketService.updateTicket(ticket.getId(), ticketService.convertToDTO(ticket)); 
        }
    }
    
    // Méthode complexe : récupérer les tickets associés à un événement
    @Transactional(readOnly = true)
    public List<Ticket> getTicketsForEvent(Long eventId) throws Exception {
        logger.info("Récupération des tickets associés à l'événement ID: {}", eventId);
        EventDTO eventDTO = eventService.getEventById(eventId);  // Récupération de l'événement via EventService
        Event event = eventService.toEntity(eventDTO);  // Conversion en entité
        return event.getTickets();  // Récupérer les tickets liés à l'événement
    }

    // Méthode complexe : assigner des offres à un événement
    @Transactional
    public void assignOffersToEvent(Long eventId, List<Long> offerIds) throws Exception {
        logger.info("Association des offres à l'événement ID: {}", eventId);
        EventDTO eventDTO = eventService.getEventById(eventId);  // Récupération de l'événement via EventService
        Event event = eventService.toEntity(eventDTO);  // Conversion en entité

        for (Long offerId : offerIds) {
            Offer offer = offerService.findById(offerId);  // Récupération de l'offre via OfferService
            EventOffer eventOffer = new EventOffer();
            eventOffer.setEvent(event);
            eventOffer.setOffer(offer);
            eventOfferRepository.save(eventOffer);  // Sauvegarde de l'association
        }
    }

    // Méthode complexe : assigner des événements à une offre
    @Transactional
    public void assignEventsToOffer(Long offerId, List<Long> eventIds) throws Exception {
        logger.info("Association des événements à l'offre ID: {}", offerId);
        Offer offer = offerService.findById(offerId);  // Récupération de l'offre via OfferService

        for (Long eventId : eventIds) {
            EventDTO eventDTO = eventService.getEventById(eventId);  // Récupération de l'événement via EventService
            Event event = eventService.toEntity(eventDTO);  // Conversion en entité

            EventOffer eventOffer = new EventOffer();
            eventOffer.setEvent(event);
            eventOffer.setOffer(offer);
            eventOfferRepository.save(eventOffer);  // Sauvegarde de l'association
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

    // Gestion des catégories : assigner une catégorie à des événements
    @Transactional
    public void assignCategoryToEvents(Long categoryId, List<Long> eventIds) throws Exception {
        logger.info("Association de la catégorie ID: {} aux événements : {}", categoryId, eventIds);
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie non trouvée"));

        if (eventIds != null && !eventIds.isEmpty()) {
            for (Long eventId : eventIds) {
                EventDTO eventDTO = eventService.getEventById(eventId);
                eventDTO.setCategory(categoryDTO);  // Associer la catégorie
                eventService.updateEvent(eventId, eventDTO);  // Sauvegarder les changements
            }
        }
    }

    // Méthode pour supprimer une association d'offre avec un événement
    @Transactional
    public void deleteEventOffer(Long eventId, Long offerId) throws Exception {
        logger.info("Suppression de l'association entre l'événement ID: {} et l'offre ID: {}", eventId, offerId);
        EventOffer eventOffer = eventOfferRepository.findByEventIdAndOfferId(eventId, offerId)
                .orElseThrow(() -> new EntityNotFoundException("Association non trouvée entre l'événement et l'offre"));
        eventOfferRepository.delete(eventOffer);  // Suppression de l'association
    }
}
