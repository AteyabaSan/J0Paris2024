package com.joparis2024.mapper;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Event;
import com.joparis2024.model.Ticket;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.stereotype.Component;


@Component
public class TicketMapper {
	
	private static final Logger logger = LoggerFactory.getLogger(TicketMapper.class);


	public TicketDTO toDTO(Ticket ticket) {
	    if (ticket == null) {
	        return null;
	    }

	    TicketDTO ticketDTO = new TicketDTO();
	    ticketDTO.setId(ticket.getId());
	    ticketDTO.setPrice(ticket.getPrice());
	    ticketDTO.setQuantity(ticket.getQuantity());  // On conserve la quantité réelle
	    ticketDTO.setAvailable(ticket.isAvailable());
	    ticketDTO.setEventDate(ticket.getEventDate());

	    // Ajouter l'Event dans le TicketDTO si disponible
	    if (ticket.getEvent() != null) {
	        EventDTO eventDTO = new EventDTO();
	        eventDTO.setId(ticket.getEvent().getId());
	        eventDTO.setEventName(ticket.getEvent().getEventName());
	        ticketDTO.setEvent(eventDTO);  // Ajout de l'EventDTO
	    }

	    return ticketDTO;
	}


	public Ticket toEntity(TicketDTO ticketDTO) {
	    if (ticketDTO == null) {
	        return null;
	    }
	    logger.info("Mapping TicketDTO to Entity - Quantity: {}", ticketDTO.getQuantity());  // Ajout du log
	    Ticket ticket = new Ticket();
	    ticket.setId(ticketDTO.getId());
	    ticket.setPrice(ticketDTO.getPrice());
	    ticket.setQuantity(ticketDTO.getQuantity());  // Retirer la condition qui met 0, si présente
	    ticket.setAvailable(ticketDTO.isAvailable());
	    ticket.setEventDate(ticketDTO.getEventDate());

	    if (ticketDTO.getEvent() != null) {
	        Event event = new Event();
	        event.setId(ticketDTO.getEvent().getId());
	        ticket.setEvent(event);
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
