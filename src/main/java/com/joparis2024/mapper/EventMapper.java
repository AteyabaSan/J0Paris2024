package com.joparis2024.mapper;


import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.OfferDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Event;
import com.joparis2024.model.Offer;
import com.joparis2024.model.Ticket;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private OfferMapper offerMapper;

    public EventDTO toDTO(Event event) throws Exception {
        if (event == null) {
            return null;
        }

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setEventName(event.getEventName());
        eventDTO.setEventDate(event.getEventDate());
        eventDTO.setDescription(event.getDescription());

        // Mapper la catégorie
        if (event.getCategory() != null) {
            eventDTO.setCategory(categoryMapper.toDTO(event.getCategory()));
        }

        // Mapper les tickets manuellement
        if (event.getTickets() != null && !event.getTickets().isEmpty()) {
            List<TicketDTO> ticketDTOs = new ArrayList<>();
            for (Ticket ticket : event.getTickets()) {
                TicketDTO ticketDTO = new TicketDTO();
                ticketDTO.setId(ticket.getId());
                ticketDTO.setPrice(ticket.getPrice());
                ticketDTOs.add(ticketDTO);
            }
            eventDTO.setTickets(ticketDTOs);
        }

        // Mapper les offres manuellement avec OfferMapper
        if (event.getOffers() != null && !event.getOffers().isEmpty()) {
            List<OfferDTO> offerDTOs = new ArrayList<>();
            for (Offer offer : event.getOffers()) {
                offerDTOs.add(offerMapper.toDTO(offer));  // Utilisation correcte de toDTO
            }
            eventDTO.setOffers(offerDTOs);
        }

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

        // Mapper la catégorie
        if (eventDTO.getCategory() != null) {
            event.setCategory(categoryMapper.toEntity(eventDTO.getCategory()));
        }

        // Mapper les tickets manuellement
        if (eventDTO.getTickets() != null && !eventDTO.getTickets().isEmpty()) {
            List<Ticket> tickets = new ArrayList<>();
            for (TicketDTO ticketDTO : eventDTO.getTickets()) {
                Ticket ticket = new Ticket();
                ticket.setId(ticketDTO.getId());
                ticket.setPrice(ticketDTO.getPrice());
                tickets.add(ticket);
            }
            event.setTickets(tickets);
        }

        // Mapper les offres manuellement avec OfferMapper
        if (eventDTO.getOffers() != null && !eventDTO.getOffers().isEmpty()) {
            List<Offer> offers = new ArrayList<>();
            for (OfferDTO offerDTO : eventDTO.getOffers()) {
                offers.add(offerMapper.toEntity(offerDTO));  // Utilisation correcte de toEntity
            }
            event.setOffers(offers);
        }

        return event;
    }
}
