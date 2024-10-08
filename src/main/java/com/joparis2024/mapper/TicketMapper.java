package com.joparis2024.mapper;

import com.joparis2024.dto.TicketDTO;
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

        // Nous nous concentrons sur le mapping des attributs simples. 
        // Les relations complexes (Event, Order) sont gérées au niveau des services, et non dans le mapper.

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

        // Pareil pour le mapping inverse, les relations sont gérées au niveau des services.

        return ticket;
    }
    
    public List<TicketDTO> toDTOs(List<Ticket> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            return new ArrayList<>();  // Retourne une liste vide si aucune donnée
        }
        
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketDTO dto = toDTO(ticket);  // Conversion via la méthode toDTO existante
            ticketDTOs.add(dto);
        }
        
        return ticketDTOs;
    }

}
