package com.joparis2024.mapper;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Event;
import com.joparis2024.model.Offer;
import com.joparis2024.model.Ticket;

import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Component;


@Component
public class TicketMapper {



	public TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setPrice(ticket.getPrice());
        ticketDTO.setQuantity(ticket.getQuantity());
        ticketDTO.setAvailable(ticket.isAvailable());
        ticketDTO.setEventDate(ticket.getEventDate());

        if (ticket.getEvent() != null) {
            EventDTO eventDTO = new EventDTO();
            eventDTO.setId(ticket.getEvent().getId());
            eventDTO.setEventName(ticket.getEvent().getEventName());
            ticketDTO.setEvent(eventDTO);
        }

        if (ticket.getOffer() != null) {  // Gérer l'Offer
            ticketDTO.setOfferId(ticket.getOffer().getId()); // On envoie juste l'ID de l'Offer
        }

        return ticketDTO;
    }

    public Ticket toEntity(TicketDTO ticketDTO) {
        if (ticketDTO == null) {
            return null;
        }

        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setQuantity(ticketDTO.getQuantity());
        ticket.setAvailable(ticketDTO.isAvailable());
        ticket.setEventDate(ticketDTO.getEventDate());

        if (ticketDTO.getEvent() != null) {
            Event event = new Event();
            event.setId(ticketDTO.getEvent().getId());
            ticket.setEvent(event);
        }

        if (ticketDTO.getOfferId() != null) {  // Gérer l'Offer
            Offer offer = new Offer();
            offer.setId(ticketDTO.getOfferId());
            ticket.setOffer(offer);  // Assigner l'Offer
        }

        return ticket;
    }
    
    public List<TicketDTO> toDTOs(List<Ticket> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            return new ArrayList<>();  
        }
        
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketDTO dto = toDTO(ticket);  
            ticketDTOs.add(dto);
        }
        
        return ticketDTOs;
    }
}
