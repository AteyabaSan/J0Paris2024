package com.joparis2024.mapper;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.model.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventMapper {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private UserMapper userMapper;

    public EventDTO toDTO(Event event) throws Exception {
        if (event == null) {
            return null;
        }

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setEventName(event.getEventName());
        eventDTO.setEventDate(event.getEventDate());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setCategory(categoryMapper.toDTO(event.getCategory())); // Utilisation de CategoryMapper
        eventDTO.setTickets(ticketMapper.toDTOs(event.getTickets())); // Utilisation de TicketMapper
        eventDTO.setOrganizer(userMapper.toDTO(event.getOrganizer())); // Utilisation de UserMapper

        return eventDTO;
    }

    public Event toEntity(EventDTO eventDTO) throws Exception {
        if (eventDTO == null) {
            return null;
        }

        Event event = new Event();
        event.setId(eventDTO.getId());
        event.setEventName(eventDTO.getEventName());
        event.setEventDate(eventDTO.getEventDate());
        event.setDescription(eventDTO.getDescription());
        event.setCategory(categoryMapper.toEntity(eventDTO.getCategory())); // Utilisation de CategoryMapper

        if (eventDTO.getTickets() != null) {
            event.setTickets(ticketMapper.toEntities(eventDTO.getTickets())); // Utilisation de TicketMapper
        }
        if (eventDTO.getOrganizer() != null) {
            event.setOrganizer(userMapper.toEntity(eventDTO.getOrganizer())); // Utilisation de UserMapper
        }

        return event;
    }

    public List<EventDTO> toDTOs(List<Event> events) throws Exception {
        if (events == null || events.isEmpty()) {
            return new ArrayList<>();
        }

        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : events) {
            eventDTOs.add(toDTO(event));
        }

        return eventDTOs;
    }

    public List<Event> toEntities(List<EventDTO> eventDTOs) throws Exception {
        if (eventDTOs == null || eventDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Event> events = new ArrayList<>();
        for (EventDTO eventDTO : eventDTOs) {
            events.add(toEntity(eventDTO));
        }

        return events;
    }
}
