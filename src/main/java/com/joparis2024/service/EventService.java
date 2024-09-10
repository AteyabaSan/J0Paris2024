package com.joparis2024.service;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.model.Event;
import com.joparis2024.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Obtenir tous les événements
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Trouver les événements par catégorie
    public List<EventDTO> getEventsByCategory(String category) {
        List<Event> events = eventRepository.findByCategory(category);
        return events.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Créer un événement
    public Event createEvent(EventDTO eventDTO) {
        Event event = mapToEntity(eventDTO); // Utilisation de la méthode de mapping vers l'entité
        return eventRepository.save(event);
    }

    // Trouver un événement par ID
    public Optional<EventDTO> findById(Long id) {
        return eventRepository.findById(id)
            .map(this::mapToDTO);
    }

    // Mettre à jour un événement
    public void updateEvent(Long id, EventDTO eventDTO) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            event.setName(eventDTO.getName());
            event.setLocation(eventDTO.getLocation());
            event.setStartTime(eventDTO.getStartTime());
            event.setEndTime(eventDTO.getEndTime());
            event.setCategory(eventDTO.getCategory());
            event.setSession(eventDTO.getSession());
            event.setDescription(eventDTO.getDescription());
            eventRepository.save(event);
        }
    }

    // Supprimer un événement
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    // Mapper l'entité Event vers un DTO EventDTO
    public EventDTO mapToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setLocation(event.getLocation());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setCategory(event.getCategory());
        dto.setSession(event.getSession());
        dto.setDescription(event.getDescription());
        return dto;
    }

    // Mapper un DTO vers l'entité Event
    private Event mapToEntity(EventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setLocation(eventDTO.getLocation());
        event.setStartTime(eventDTO.getStartTime());
        event.setEndTime(eventDTO.getEndTime());
        event.setCategory(eventDTO.getCategory());
        event.setSession(eventDTO.getSession());
        event.setDescription(eventDTO.getDescription());
        return event;
    }
}
