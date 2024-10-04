package com.joparis2024.service;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.model.Event;
import com.joparis2024.repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.hibernate.Hibernate;
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
    @Lazy
    private TicketService ticketService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private CategoryService categoryService;

    @Transactional(readOnly = true)
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : events) {
            try {
                // Initialiser les relations Lazy avant de mapper en DTO
                Hibernate.initialize(event.getTickets());
                Hibernate.initialize(event.getOrganizer());
                
                eventDTOs.add(mapToDTO(event));
            } catch (Exception e) {
                logger.error("Erreur lors du mapping de l'événement avec ID: {}", event.getId(), e);
            }
        }
        return eventDTOs;
    }
    
    @Transactional
    public Event createEvent(EventDTO eventDTO) throws Exception {
        validateEventDTO(eventDTO);

        Event event = mapToEntity(eventDTO);

        return eventRepository.save(event);
    }

    private void validateEventDTO(EventDTO eventDTO) {
        if (eventDTO.getEventName() == null || eventDTO.getEventName().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'événement ne peut pas être vide");
        }
    }

    @Transactional(readOnly = true)
    public Optional<EventDTO> getEventByName(String eventName) {
        return eventRepository.findByEventName(eventName)
                .map(t -> {
                    try {
                        // Initialiser les relations Lazy avant de mapper en DTO
                        Hibernate.initialize(t.getTickets());
                        Hibernate.initialize(t.getOrganizer());
                        
                        return mapToDTO(t);
                    } catch (Exception e) {
                        logger.error("Erreur lors du mapping de l'événement avec nom : {}", eventName, e);
                        return null;
                    }
                });
    }

    @Transactional
    public EventDTO updateEventByName(String eventName, EventDTO eventDTO) throws Exception {
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new EntityNotFoundException("Événement non trouvé avec le nom : " + eventName));

        updateEventAttributes(event, eventDTO);

        return mapToDTO(eventRepository.save(event));
    }

    private void updateEventAttributes(Event event, EventDTO eventDTO) throws Exception {
        event.setEventName(eventDTO.getEventName());
        event.setEventDate(eventDTO.getEventDate());
        event.setDescription(eventDTO.getDescription());
        
        if (eventDTO.getTickets() != null) {
            event.setTickets(ticketService.mapToEntities(eventDTO.getTickets()));
        }
        
        if (eventDTO.getOrganizer() != null) {
            event.setOrganizer(userService.mapToEntity(eventDTO.getOrganizer()));
        }
        
        if (eventDTO.getCategory() != null) {
            event.setCategory(categoryService.mapToEntity(eventDTO.getCategory()));
        }
    }
    
    @Transactional(readOnly = true)
    public EventDTO mapToDTO(Event event) throws Exception {
        // Initialiser les relations Lazy avant de mapper en DTO
        Hibernate.initialize(event.getTickets());
        Hibernate.initialize(event.getOrganizer());
        
        return new EventDTO(
                event.getId(),
                event.getEventName(),
                event.getEventDate(),
                event.getDescription(),
                categoryService.mapToDTO(event.getCategory()),
                ticketService.mapToDTOs(event.getTickets()),
                userService.mapToDTO(event.getOrganizer())
        );
    }

    public Event mapToEntity(EventDTO eventDTO) throws Exception {
        Event event = new Event();
        if (eventDTO.getId() != null) {
            event.setId(eventDTO.getId());
        }

        event.setEventName(eventDTO.getEventName());
        event.setEventDate(eventDTO.getEventDate());
        event.setDescription(eventDTO.getDescription());

        event.setCategory(categoryService.mapToEntity(eventDTO.getCategory()));

        if (eventDTO.getTickets() != null) {
            event.setTickets(ticketService.mapToEntities(eventDTO.getTickets()));
        }

        if (eventDTO.getOrganizer() != null) {
            event.setOrganizer(userService.mapToEntity(eventDTO.getOrganizer()));
        }

        return event;
    }

    @Transactional
    public void deleteEvent(String eventName) throws Exception {
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new Exception("Événement non trouvé"));
        eventRepository.delete(event);
    }
    
    @Transactional(readOnly = true)
    public List<Event> mapToEntities(List<EventDTO> eventDTOs) throws Exception {
        List<Event> events = new ArrayList<>();
        if (eventDTOs != null) {
            for (EventDTO eventDTO : eventDTOs) {
                events.add(mapToEntity(eventDTO));  // Utilise la méthode mapToEntity déjà présente
            }
        } else {
            throw new Exception("La liste des EventDTOs est vide");
        }
        return events;
    }

    @Transactional(readOnly = true)
    public List<EventDTO> mapToDTOs(List<Event> events) throws Exception {
        List<EventDTO> eventDTOs = new ArrayList<>();
        if (events != null) {
            for (Event event : events) {
                // Initialiser les relations Lazy avant de mapper en DTO
                Hibernate.initialize(event.getTickets());
                Hibernate.initialize(event.getOrganizer());
                
                eventDTOs.add(mapToDTO(event));  // Utilise la méthode mapToDTO déjà présente
            }
        } else {
            throw new Exception("La liste des événements est vide");
        }
        return eventDTOs;
    }
}


