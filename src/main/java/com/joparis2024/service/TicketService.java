package com.joparis2024.service;

import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Event;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    // Récupérer tous les tickets
    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Créer un ticket
    public Ticket createTicket(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setEvent(new Event(ticketDTO.getEventId()));
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setSeatNumber(ticketDTO.getSeatNumber());
        ticket.setIsAvailable(ticketDTO.getIsAvailable());
        return ticketRepository.save(ticket);
    }

    // Récupérer un ticket par ID
    public Optional<TicketDTO> getTicketById(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        return ticket.map(this::mapToDTO);
    }

    // Mapper l'entité Ticket vers un DTO TicketDTO
    private TicketDTO mapToDTO(Ticket ticket) {
        return new TicketDTO(ticket.getId(), ticket.getEvent().getId(), ticket.getOwner().getId(), ticket.getPrice(), ticket.getSeatNumber(), ticket.getIsAvailable());
    }
}
