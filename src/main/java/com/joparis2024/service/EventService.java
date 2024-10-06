package com.joparis2024.service;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.EventOfferDTO;
import com.joparis2024.dto.OfferDTO;
import com.joparis2024.mapper.EventMapper;
import com.joparis2024.model.Event;
import com.joparis2024.repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventOfferService eventOfferService; // Ajout du service EventOffer

    @Transactional(readOnly = true)
    public List<EventDTO> getAllEvents() {
        logger.info("Récupération de tous les événements");
        List<Event> events = eventRepository.findAll();
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : events) {
            try {
                eventDTOs.add(eventMapper.toDTO(event));
            } catch (Exception e) {
                logger.error("Erreur lors du mapping de l'événement : {}", event.getEventName(), e);
            }
        }
        logger.info("Nombre d'événements récupérés: {}", eventDTOs.size());
        return eventDTOs;
    }

    @Transactional
    public EventDTO createEvent(EventDTO eventDTO) throws Exception {
        logger.info("Tentative de création d'un nouvel événement : {}", eventDTO.getEventName());
        validateEventDTO(eventDTO);

        Event event = eventMapper.toEntity(eventDTO);
        Event savedEvent = eventRepository.save(event);
        
        // Gestion des relations avec les offres via EventOffer
        if (eventDTO.getOffers() != null && !eventDTO.getOffers().isEmpty()) {
            for (OfferDTO offerDTO : eventDTO.getOffers()) {
                EventOfferDTO eventOfferDTO = new EventOfferDTO();
                eventOfferDTO.setEvent(eventMapper.toDTO(savedEvent));
                eventOfferDTO.setOffer(offerDTO);
                eventOfferService.createEventOffer(eventOfferDTO); // Créer l'association Event-Offer
            }
        }

        logger.info("Événement créé avec succès : {}", savedEvent.getEventName());
        return eventMapper.toDTO(savedEvent);
    }

    @Transactional
    public EventDTO updateEventByName(String eventName, EventDTO eventDTO) throws Exception {
        logger.info("Mise à jour de l'événement : {}", eventName);
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new EntityNotFoundException("Événement non trouvé avec le nom : " + eventName));

        event.setEventName(eventDTO.getEventName());
        event.setEventDate(eventDTO.getEventDate());
        event.setDescription(eventDTO.getDescription());

        Event updatedEvent = eventRepository.save(event);

        // Gestion des relations avec les offres via EventOffer
        if (eventDTO.getOffers() != null && !eventDTO.getOffers().isEmpty()) {
            for (OfferDTO offerDTO : eventDTO.getOffers()) {
                EventOfferDTO eventOfferDTO = new EventOfferDTO();
                eventOfferDTO.setEvent(eventMapper.toDTO(updatedEvent));
                eventOfferDTO.setOffer(offerDTO);
                eventOfferService.createEventOffer(eventOfferDTO); // Créer l'association Event-Offer
            }
        }

        logger.info("Événement mis à jour avec succès : {}", updatedEvent.getEventName());
        return eventMapper.toDTO(updatedEvent);
    }

    @Transactional
    public void deleteEvent(String eventName) throws Exception {
        logger.info("Tentative de suppression de l'événement : {}", eventName);
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new Exception("Événement non trouvé"));
        eventRepository.delete(event);
        logger.info("Événement supprimé avec succès : {}", eventName);
    }

    @Transactional(readOnly = true)
    public Event findById(Long id) throws Exception {
        logger.info("Recherche de l'événement avec l'ID : {}", id);
        return eventRepository.findById(id)
                .orElseThrow(() -> new Exception("Événement non trouvé avec l'id : " + id));
    }

    private void validateEventDTO(EventDTO eventDTO) {
        if (eventDTO.getEventName() == null || eventDTO.getEventName().isEmpty()) {
            logger.error("Le nom de l'événement est manquant");
            throw new IllegalArgumentException("Le nom de l'événement ne peut pas être vide");
        }
    }
}
