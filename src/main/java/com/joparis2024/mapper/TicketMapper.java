package com.joparis2024.mapper;

import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Ticket;
import org.springframework.context.annotation.Lazy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TicketMapper {

    @Autowired
    @Lazy
    private EventMapper eventMapper;

    @Autowired
    private OrderMapper orderMapper;

    public TicketDTO toDTO(Ticket ticket) throws Exception {
        if (ticket == null) {
            return null;
        }

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setEvent(eventMapper.toDTO(ticket.getEvent()));
        ticketDTO.setOrder(orderMapper.toDTO(ticket.getOrder()));
        ticketDTO.setPrice(ticket.getPrice());
        ticketDTO.setQuantity(ticket.getQuantity());
        ticketDTO.setAvailable(ticket.isAvailable());
        ticketDTO.setEventDate(ticket.getEventDate());

        return ticketDTO;
    }

    public Ticket toEntity(TicketDTO ticketDTO) throws Exception {
        if (ticketDTO == null) {
            return null;
        }

        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        ticket.setEvent(eventMapper.toEntity(ticketDTO.getEvent()));
        ticket.setOrder(orderMapper.toEntity(ticketDTO.getOrder()));
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setQuantity(ticketDTO.getQuantity());
        ticket.setAvailable(ticketDTO.isAvailable());
        ticket.setEventDate(ticketDTO.getEventDate());

        return ticket;
    }

    public List<TicketDTO> toDTOs(List<Ticket> tickets) throws Exception {
        if (tickets == null || tickets.isEmpty()) {
            return new ArrayList<>();
        }

        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketDTOs.add(toDTO(ticket));
        }

        return ticketDTOs;
    }

    public List<Ticket> toEntities(List<TicketDTO> ticketDTOs) throws Exception {
        if (ticketDTOs == null || ticketDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Ticket> tickets = new ArrayList<>();
        for (TicketDTO ticketDTO : ticketDTOs) {
            tickets.add(toEntity(ticketDTO));
        }

        return tickets;
    }
}
