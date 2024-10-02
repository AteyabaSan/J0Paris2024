package com.joparis2024.service;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.model.Category;
import com.joparis2024.model.Event;
import com.joparis2024.repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    @Lazy // Injection différée pour casser la dépendance circulaire
    private TicketService ticketService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private CategoryService categoryService;

    // Récupérer tous les événements
    // Ajouter @Transactional ici pour garder la session active
    @Transactional
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : events) {
            try {
                eventDTOs.add(mapToDTO(event));
            } catch (Exception e) {
                System.out.println("Erreur lors du mapping de l'événement : " + event.getId());
                e.printStackTrace();  // Log de l'erreur pour debug
            }
        }
        return eventDTOs;
    }

    // Créer un événement
    public Event createEvent(EventDTO eventDTO) {
        try {
            // Validation des données d'entrée
            if (eventDTO.getEventName() == null || eventDTO.getEventName().isEmpty()) {
                throw new IllegalArgumentException("Le nom de l'événement ne peut pas être vide");
            }

            // Mappage DTO -> Entité
            Event event = mapToEntity(eventDTO);

            // Sauvegarde dans la base de données
            return eventRepository.save(event);

        } catch (Exception e) {
            System.out.println("Erreur lors de la création de l'événement");
            e.printStackTrace();
            throw new RuntimeException("Erreur inconnue lors de la création de l'événement");
        }
    }

    // Récupérer un événement par nom
    @Transactional
    public Optional<EventDTO> getEventByName(String eventName) {
        Optional<Event> event = eventRepository.findByEventName(eventName);
        return event.map(t -> {
            try {
                return mapToDTO(t);
            } catch (Exception e) {
                System.out.println("Erreur lors du mapping de l'événement avec nom : " + eventName);
                e.printStackTrace();
                return null;
            }
        });
    }

    // Mettre à jour un événement via son nom (UPDATE)
    public EventDTO updateEventByName(String eventName, EventDTO eventDTO) throws EntityNotFoundException, Exception {
        // Recherche de l'événement par son nom
        Optional<Event> existingEvent = eventRepository.findByEventName(eventName);
        if (!existingEvent.isPresent()) {
            throw new EntityNotFoundException("Événement non trouvé avec le nom : " + eventName);
        }

        Event event = existingEvent.get();
        
        // Mise à jour des attributs de base
        event.setEventName(eventDTO.getEventName());
        event.setEventDate(eventDTO.getEventDate());
        event.setDescription(eventDTO.getDescription());

        // Mise à jour des relations (tickets, organizer, category)
        if (eventDTO.getTickets() != null) {
            event.setTickets(ticketService.mapToEntities(eventDTO.getTickets()));
        }
        
        if (eventDTO.getOrganizer() != null) {
            event.setOrganizer(userService.mapToEntity(eventDTO.getOrganizer()));
        }
        
        if (eventDTO.getCategory() != null) {
            event.setCategory(categoryService.mapToEntity(eventDTO.getCategory()));
        }

        // Sauvegarde de l'événement mis à jour
        Event updatedEvent = eventRepository.save(event);

        // Retourne un DTO après la mise à jour
        return mapToDTO(updatedEvent);  // Conversion de l'entité en DTO
    }




 // Mapper l'entité Event vers un DTO EventDTO
    @Transactional
    public EventDTO mapToDTO(Event event) throws Exception {
        try {
            return new EventDTO(
                    event.getId(),
                    event.getEventName(),
                    event.getEventDate(),
                    event.getDescription(),
                    categoryService.mapToDTO(event.getCategory()),  // Utilisation correcte du CategoryService
                    ticketService.mapToDTOs(event.getTickets()),     // Mapping des tickets
                    userService.mapToDTO(event.getOrganizer())       // Mapping de l'organisateur
            );
        } catch (Exception e) {
            throw new Exception("Erreur lors du mapping de l'événement en DTO", e);
        }
    }
    
 // Mappage d'une liste d'Event en liste de EventDTO
    public List<EventDTO> mapToDTOs(List<Event> events) throws Exception {
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : events) {
            eventDTOs.add(mapToDTO(event));
        }
        return eventDTOs;
    }

 // Mapper le DTO EventDTO vers l'entité Event
    public Event mapToEntity(EventDTO eventDTO) throws Exception {
        if (eventDTO == null) {
            throw new Exception("L'EventDTO est manquant ou invalide.");
        }

        Event event = new Event();

        // Vérifier et attribuer l'ID de l'événement s'il existe
        if (eventDTO.getId() != null) {
            event.setId(eventDTO.getId());
        }

        event.setEventName(eventDTO.getEventName());
        event.setEventDate(eventDTO.getEventDate());
        event.setDescription(eventDTO.getDescription());

        // Utilisation de CategoryService pour mapper CategoryDTO -> Category
        Category category = categoryService.mapToEntity(eventDTO.getCategory());
        event.setCategory(category);

        // Gestion des tickets
        if (eventDTO.getTickets() != null && !eventDTO.getTickets().isEmpty()) {
            event.setTickets(ticketService.mapToEntities(eventDTO.getTickets()));
        } else {
            event.setTickets(new ArrayList<>());  // Initialiser une liste vide si pas de tickets
        }

        // Gestion de l'organisateur
        if (eventDTO.getOrganizer() != null) {
            // Si un ID est fourni, on cherche l'organisateur avec cet ID
            if (eventDTO.getOrganizer().getId() != null) {
                event.setOrganizer(userService.mapToEntity(eventDTO.getOrganizer()));
            } else {
                // Si pas d'ID, on lève une exception
                throw new Exception("L'organisateur de l'événement doit être fourni avec un ID valide.");
            }
        } else {
            throw new Exception("L'organisateur de l'événement est manquant ou invalide.");
        }

        return event;
    }
    
    
 // Mappage d'une liste de EventDTO en liste d'entités Event
    public List<Event> mapToEntities(List<EventDTO> eventDTOs) throws Exception {
        List<Event> events = new ArrayList<>();
        for (EventDTO dto : eventDTOs) {
            events.add(mapToEntity(dto));
        }
        return events;
    }

    // Supprimer un événement par nom
    public void deleteEvent(String eventName) throws Exception {
        Optional<Event> event = eventRepository.findByEventName(eventName);
        if (!event.isPresent()) {
            throw new Exception("Événement non trouvé");
        }
        eventRepository.delete(event.get());
    }
}

