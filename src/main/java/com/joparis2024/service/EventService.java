package com.joparis2024.service;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.mapper.CategoryMapper;
import com.joparis2024.mapper.EventMapper;
import com.joparis2024.mapper.TicketMapper;
import com.joparis2024.mapper.UserMapper;
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
import java.util.Optional;

@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    // Récupérer tous les événements
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

    // Créer un nouvel événement
    @Transactional
    public EventDTO createEvent(EventDTO eventDTO) throws Exception {
        logger.info("Tentative de création d'un nouvel événement : {}", eventDTO.getEventName());
        validateEventDTO(eventDTO);
        Event event = eventMapper.toEntity(eventDTO);
        Event savedEvent = eventRepository.save(event);
        logger.info("Événement créé avec succès : {}", savedEvent.getEventName());
        return eventMapper.toDTO(savedEvent);
    }

    // Valider les données de l'événement
    private void validateEventDTO(EventDTO eventDTO) {
        if (eventDTO.getEventName() == null || eventDTO.getEventName().isEmpty()) {
            logger.error("Le nom de l'événement est manquant");
            throw new IllegalArgumentException("Le nom de l'événement ne peut pas être vide");
        }
    }

    // Récupérer un événement par son nom
    @Transactional(readOnly = true)
    public Optional<EventDTO> getEventByName(String eventName) {
        logger.info("Recherche de l'événement par nom : {}", eventName);
        return eventRepository.findByEventName(eventName)
                .map(event -> {
                    try {
                        return eventMapper.toDTO(event);
                    } catch (Exception e) {
                        logger.error("Erreur lors du mapping de l'événement : {}", eventName, e);
                        return null;
                    }
                });
    }

    // Mettre à jour un événement par son nom
    @Transactional
    public EventDTO updateEventByName(String eventName, EventDTO eventDTO) throws Exception {
        logger.info("Mise à jour de l'événement : {}", eventName);
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new EntityNotFoundException("Événement non trouvé avec le nom : " + eventName));

        updateEventAttributes(event, eventDTO);
        Event updatedEvent = eventRepository.save(event);
        logger.info("Événement mis à jour avec succès : {}", updatedEvent.getEventName());
        return eventMapper.toDTO(updatedEvent);
    }

    // Mise à jour des attributs de l'événement
    private void updateEventAttributes(Event event, EventDTO eventDTO) throws Exception {
        event.setEventName(eventDTO.getEventName());
        event.setEventDate(eventDTO.getEventDate());
        event.setDescription(eventDTO.getDescription());

        if (eventDTO.getTickets() != null) {
            event.setTickets(ticketMapper.toEntities(eventDTO.getTickets()));
            logger.info("Mise à jour des tickets pour l'événement : {}", event.getEventName());
        }

        if (eventDTO.getOrganizer() != null) {
            event.setOrganizer(userMapper.toEntity(eventDTO.getOrganizer()));
            logger.info("Mise à jour de l'organisateur pour l'événement : {}", event.getEventName());
        }

        if (eventDTO.getCategory() != null) {
            event.setCategory(categoryMapper.toEntity(eventDTO.getCategory()));
            logger.info("Mise à jour de la catégorie pour l'événement : {}", event.getEventName());
        }
    }

    // Supprimer un événement par son nom
    @Transactional
    public void deleteEvent(String eventName) throws Exception {
        logger.info("Tentative de suppression de l'événement : {}", eventName);
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new Exception("Événement non trouvé"));
        eventRepository.delete(event);
        logger.info("Événement supprimé avec succès : {}", eventName);
    }

    // Récupérer un événement par son ID
    @Transactional(readOnly = true)
    public Event findById(Long id) throws Exception {
        logger.info("Recherche de l'événement avec l'ID : {}", id);
        return eventRepository.findById(id)
                .orElseThrow(() -> new Exception("Événement non trouvé avec l'id : " + id));
    }
}
