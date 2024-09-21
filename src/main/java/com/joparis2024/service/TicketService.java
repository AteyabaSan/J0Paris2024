package com.joparis2024.service;

import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    @Lazy // Injection différée pour éviter la boucle
    private EventService eventService;

    @Autowired
    private OrderService orderService;

    // Créer un ticket
    public Ticket createTicket(TicketDTO ticketDTO) throws Exception {
        System.out.println("Création d'un ticket pour l'événement ID: " + ticketDTO.getEvent().getId());
        Ticket ticket = new Ticket();
        ticket.setEvent(eventService.mapToEntity(ticketDTO.getEvent()));  // Mapper l'événement
        ticket.setOrder(orderService.mapToEntity(ticketDTO.getOrder()));  // Mapper la commande
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setQuantity(ticketDTO.getQuantity());
        ticket.setAvailable(ticketDTO.isAvailable());
        
        Ticket savedTicket = ticketRepository.save(ticket);
        System.out.println("Ticket créé avec succès, ID: " + savedTicket.getId());
        return savedTicket;
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
    public List<TicketDTO> getAllTickets() throws Exception {
        List<Ticket> tickets = ticketRepository.findAll();
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketDTOs.add(mapToDTO(ticket));
        }
        return ticketDTOs;
    }

    // Mapper Ticket -> TicketDTO
    public TicketDTO mapToDTO(Ticket ticket) throws Exception {
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
    public Ticket mapToEntity(TicketDTO ticketDTO) throws Exception {
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
    public List<TicketDTO> mapToDTOs(List<Ticket> tickets) throws Exception {
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        if (tickets != null) {
            for (Ticket ticket : tickets) {
                ticketDTOs.add(mapToDTO(ticket));
            }
        } else {
            throw new Exception("La liste de tickets est vide");
        }
        return ticketDTOs;
    }

    // Convertir une liste de TicketDTOs en une liste de Tickets
    public List<Ticket> mapToEntities(List<TicketDTO> ticketDTOs) throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        if (ticketDTOs != null) {
            for (TicketDTO ticketDTO : ticketDTOs) {
                tickets.add(mapToEntity(ticketDTO));
            }
        } else {
            throw new Exception("La liste de TicketDTOs est vide");
        }
        return tickets;
    }

}
