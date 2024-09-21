package com.joparis2024.service;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.model.Event;
import com.joparis2024.repository.EventRepository;
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

    // Récupérer tous les événements avec boucle for
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : events) {
            try {
                eventDTOs.add(mapToDTO(event));
            } catch (Exception e) {
                System.out.println("Erreur lors du mapping de l'événement : " + event.getId());
                e.printStackTrace();  // Log l'erreur pour debug
            }
        }
        return eventDTOs;
    }

    // Créer un événement
    public Event createEvent(EventDTO eventDTO) {
        try {
            Event event = mapToEntity(eventDTO);
            return eventRepository.save(event);
        } catch (Exception e) {
            System.out.println("Erreur lors de la création de l'événement");
            e.printStackTrace();
            return null;  // Ou déclenche une exception personnalisée si nécessaire
        }
    }

    // Récupérer un événement par nom
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

    // Récupérer les événements par catégorie avec boucle for
    public List<EventDTO> getEventsByCategory(String category) {
        List<Event> events = eventRepository.findByCategory(category);
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : events) {
            try {
                eventDTOs.add(mapToDTO(event));
            } catch (Exception e) {
                System.out.println("Erreur lors du mapping de l'événement : " + event.getId());
                e.printStackTrace();
            }
        }
        return eventDTOs;
    }

    // Mettre à jour un événement
    public Event updateEvent(Long eventId, EventDTO eventDTO) throws Exception {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        if (!existingEvent.isPresent()) {
            throw new Exception("Événement non trouvé");
        }
        Event event = existingEvent.get();
        event.setEventName(eventDTO.getEventName());
        event.setDate(eventDTO.getDate());
        event.setLocation(eventDTO.getLocation());
        event.setCategory(eventDTO.getCategory());
        event.setPriceRange(eventDTO.getPriceRange());
        event.setAvailableTickets(eventDTO.getAvailableTickets());
        event.setDescription(eventDTO.getDescription());
        event.setSoldOut(eventDTO.isSoldOut());

        // Mise à jour des relations
        event.setTickets(ticketService.mapToEntities(eventDTO.getTickets()));
        event.setOrganizer(userService.mapToEntity(eventDTO.getOrganizer()));

        return eventRepository.save(event);
    }

    // Mapper l'entité Event vers un DTO EventDTO
    public EventDTO mapToDTO(Event event) throws Exception {
        try {
            return new EventDTO(
                    event.getId(),
                    event.getEventName(),
                    event.getDate(),
                    event.getLocation(),
                    event.getCategory(),
                    event.getPriceRange(),
                    event.getAvailableTickets(),
                    event.getDescription(),
                    event.isSoldOut(),
                    ticketService.mapToDTOs(event.getTickets()), // Le mapping peut lancer une exception
                    userService.mapToDTO(event.getOrganizer())
            );
        } catch (Exception e) {
            throw new Exception("Erreur lors du mapping de l'événement en DTO", e);
        }
    }


    // Mapper le DTO EventDTO vers l'entité Event
    public Event mapToEntity(EventDTO eventDTO) throws Exception {
        try {
            Event event = new Event();
            event.setId(eventDTO.getId());
            event.setEventName(eventDTO.getEventName());
            event.setDate(eventDTO.getDate());
            event.setLocation(eventDTO.getLocation());
            event.setCategory(eventDTO.getCategory());
            event.setPriceRange(eventDTO.getPriceRange());
            event.setAvailableTickets(eventDTO.getAvailableTickets());
            event.setDescription(eventDTO.getDescription());
            event.setSoldOut(eventDTO.isSoldOut());

            // Ajout des relations
            event.setTickets(ticketService.mapToEntities(eventDTO.getTickets()));
            event.setOrganizer(userService.mapToEntity(eventDTO.getOrganizer()));

            return event;
        } catch (Exception e) {
            throw new Exception("Erreur lors du mapping du DTO en entité Event", e);
        }
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
