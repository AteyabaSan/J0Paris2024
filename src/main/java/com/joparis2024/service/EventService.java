package com.joparis2024.service;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.mapper.EventMapper;
import com.joparis2024.model.Category;
import com.joparis2024.model.Event;
import com.joparis2024.repository.CategoryRepository;
import com.joparis2024.repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Transactional(readOnly = true)
    public List<EventDTO> getAllEvents() {
        logger.info("Récupération de tous les événements");
        List<Event> events = eventRepository.findAll();
        return eventMapper.toDTOs(events);  // Conversion des entités en DTO via le mapper
    }

    @Transactional
    public EventDTO createEvent(EventDTO eventDTO) {
        // Valider les données du DTO
        validateEventDTO(eventDTO);

        // Récupérer la catégorie depuis l'ID
        Category category = categoryRepository.findById(eventDTO.getCategoryId())
            .orElseThrow(() -> new IllegalArgumentException("Catégorie non trouvée pour l'ID: " + eventDTO.getCategoryId()));

        // Convertir DTO en entité Event
        Event event = new Event();
        event.setEventName(eventDTO.getEventName());
        event.setEventDate(eventDTO.getEventDate());
        event.setDescription(eventDTO.getDescription());
        event.setCategory(category); // Associer la catégorie récupérée

        // Sauvegarder l'événement et récupérer l'ID généré
        Event savedEvent = eventRepository.save(event);

        // Mettre à jour l'EventDTO avec l'ID généré
        eventDTO.setId(savedEvent.getId());

        // Retourner l'EventDTO mis à jour avec l'ID
        return eventDTO;
    }

    @Transactional
    public EventDTO updateEvent(Long eventId, EventDTO eventDTO) throws Exception {
        logger.info("Mise à jour de l'événement avec ID: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Événement non trouvé"));

        // Mise à jour des attributs de l'événement
        event.setEventName(eventDTO.getEventName());
        event.setEventDate(eventDTO.getEventDate());
        event.setDescription(eventDTO.getDescription());

        // La catégorie est gérée via le DTO et le mapper, donc aucune logique d'association directe
        Event updatedEvent = eventRepository.save(event);

        return eventMapper.toDTO(updatedEvent);  // Conversion en DTO
    }

    @Transactional
    public void deleteEvent(Long eventId) throws Exception {
        logger.info("Suppression de l'événement avec ID: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Événement non trouvé"));
        eventRepository.delete(event);
    }

    @Transactional(readOnly = true)
    public EventDTO getEventById(Long eventId) throws Exception {
        logger.info("Récupération de l'événement avec ID: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Événement non trouvé"));
        return eventMapper.toDTO(event);  // Conversion en DTO
    }

    private void validateEventDTO(EventDTO eventDTO) {
        if (eventDTO.getEventName() == null || eventDTO.getEventName().isEmpty()) {
            logger.error("Le nom de l'événement est manquant");
            throw new IllegalArgumentException("Le nom de l'événement ne peut pas être vide");
        }

        if (eventDTO.getCategoryId() == null) {
            logger.error("La catégorie de l'événement est manquante");
            throw new IllegalArgumentException("La catégorie de l'événement ne peut pas être vide");
        }

        // Ajouter d'autres validations si nécessaire (ex : date de l'événement, description, etc.)
    }


    // Méthode pour convertir un DTO Event en entité Event
    public Event toEntity(EventDTO eventDTO) {
        return eventMapper.toEntity(eventDTO);  // Utilisation du mapper pour la conversion
    }
}
