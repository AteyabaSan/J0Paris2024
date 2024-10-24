package com.joparis2024.mapper;


import com.joparis2024.dto.EventDTO;
import com.joparis2024.model.Event;
import com.joparis2024.model.Offer;
import com.joparis2024.model.Ticket;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventDTO toDTO(Event event) {
        if (event == null) {
            return null;
        }

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setEventName(event.getEventName());
        eventDTO.setEventDate(event.getEventDate());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setLocation(event.getLocation());
        // Utiliser l'ID de la catégorie
        if (event.getCategory() != null) {
            eventDTO.setCategoryId(event.getCategory().getId());
        }

        // Mapper les tickets avec juste les IDs
        if (event.getTickets() != null) {
            List<Long> ticketIds = new ArrayList<>();
            for (Ticket ticket : event.getTickets()) {
                ticketIds.add(ticket.getId());
            }
            eventDTO.setTicketIds(ticketIds);
        }

        // Mapper les offres avec juste les IDs
        if (event.getOffers() != null) {
            List<Long> offerIds = new ArrayList<>();
            for (Offer offer : event.getOffers()) {
                offerIds.add(offer.getId());
            }
            eventDTO.setOfferIds(offerIds);
        }

        return eventDTO;
    }

    public Event toEntity(EventDTO eventDTO) {
        if (eventDTO == null) {
            return null;
        }

        Event event = new Event();
        event.setId(eventDTO.getId());
        event.setEventName(eventDTO.getEventName());
        event.setEventDate(eventDTO.getEventDate());
        event.setDescription(eventDTO.getDescription());

        // Remarque: les relations avec Category, Tickets et Offers ne sont pas gérées ici.
        // Cela sera géré au niveau des services et des facades.

        return event;
    }
    
    // Méthode pour convertir une liste d'entités Event en EventDTO
    public List<EventDTO> toDTOs(List<Event> events) {
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : events) {
            eventDTOs.add(toDTO(event));  // Appel de la méthode de conversion d'un seul événement
        }
        return eventDTOs;
    }
}
