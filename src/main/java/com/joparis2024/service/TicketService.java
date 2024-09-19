package com.joparis2024.service;

import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private OrderService orderService;

    // Créer un ticket
    public Ticket createTicket(TicketDTO ticketDTO) throws Exception {
        Ticket ticket = new Ticket();
        ticket.setEvent(eventService.mapToEntity(ticketDTO.getEvent()));  // Mapper l'événement
        ticket.setOrder(orderService.mapToEntity(ticketDTO.getOrder()));  // Mapper la commande
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setQuantity(ticketDTO.getQuantity());
        ticket.setAvailable(ticketDTO.isAvailable());
        return ticketRepository.save(ticket);
    }

    // Mettre à jour un ticket
    public Ticket updateTicket(Long ticketId, TicketDTO ticketDTO) throws Exception {
        Optional<Ticket> existingTicket = ticketRepository.findById(ticketId);
        if (!existingTicket.isPresent()) {
            throw new Exception("Ticket non trouvé");
        }

        Ticket ticket = existingTicket.get();
        ticket.setEvent(eventService.mapToEntity(ticketDTO.getEvent()));  // Mapper l'événement
        ticket.setOrder(orderService.mapToEntity(ticketDTO.getOrder()));  // Mapper la commande
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setQuantity(ticketDTO.getQuantity());
        ticket.setAvailable(ticketDTO.isAvailable());
        return ticketRepository.save(ticket);
    }

    // Récupérer tous les tickets
    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketDTOs.add(mapToDTO(ticket));
        }
        return ticketDTOs;
    }

    // Mapper Ticket -> TicketDTO
    public TicketDTO mapToDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());  // Ajout de l'ID
        ticketDTO.setEvent(eventService.mapToDTO(ticket.getEvent()));  // Mapper l'événement
        ticketDTO.setOrder(orderService.mapToDTO(ticket.getOrder()));  // Mapper la commande
        ticketDTO.setPrice(ticket.getPrice());
        ticketDTO.setQuantity(ticket.getQuantity());
        ticketDTO.setAvailable(ticket.isAvailable());
        return ticketDTO;
    }

    // Mapper TicketDTO -> Ticket (Entity)
    public Ticket mapToEntity(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());  // Ajout de l'ID
        ticket.setEvent(eventService.mapToEntity(ticketDTO.getEvent()));  // Mapper l'événement
        ticket.setOrder(orderService.mapToEntity(ticketDTO.getOrder()));  // Mapper la commande
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setQuantity(ticketDTO.getQuantity());
        ticket.setAvailable(ticketDTO.isAvailable());
        return ticket;
    }
    
 // Convertir une liste de Tickets en une liste de TicketDTOs
    public List<TicketDTO> mapToDTOs(List<Ticket> tickets) {
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketDTOs.add(mapToDTO(ticket));
        }
        return ticketDTOs;
    }

    // Convertir une liste de TicketDTOs en une liste de Tickets
    public List<Ticket> mapToEntities(List<TicketDTO> ticketDTOs) {
        List<Ticket> tickets = new ArrayList<>();
        for (TicketDTO ticketDTO : ticketDTOs) {
            tickets.add(mapToEntity(ticketDTO));
        }
        return tickets;
    }

}
