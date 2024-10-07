package com.joparis2024.service;

import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.TicketMapper;
import com.joparis2024.model.Event;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.TicketRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private EventService eventService;

    @Autowired
    private OrderTicketFacade orderTicketFacade;  // Utilisation de la façade

    @Transactional
    public TicketDTO createTicket(TicketDTO ticketDTO) throws Exception {
        logger.info("Tentative de création d'un ticket : {}", ticketDTO);

        // Utilisation du service pour récupérer l'entité Event
        Event event = eventService.findById(ticketDTO.getEvent().getId());

        // Mapper pour obtenir l'entité Ticket
        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        ticket.setEvent(event);  // Liaison directe avec l'événement

        // Sauvegarde du ticket
        Ticket savedTicket = ticketRepository.save(ticket);
        logger.info("Ticket créé avec succès : {}", savedTicket.getId());

        // Utilisation de la façade pour lier le ticket à une commande (si une commande est présente)
        if (ticketDTO.getOrder() != null) {
            orderTicketFacade.assignTicketToOrder(savedTicket.getId(), ticketDTO.getOrder().getId());
        }

        return ticketMapper.toDTO(savedTicket);
    }

    @Transactional
    public TicketDTO updateTicket(Long ticketId, TicketDTO ticketDTO) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trouvé"));

        // Utilisation des services pour les entités liées
        Event event = eventService.findById(ticketDTO.getEvent().getId());

        ticket.setEvent(event);
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setQuantity(ticketDTO.getQuantity());
        ticket.setAvailable(ticketDTO.isAvailable());

        Ticket updatedTicket = ticketRepository.save(ticket);
        logger.info("Ticket mis à jour avec succès : {}", updatedTicket.getId());

        return ticketMapper.toDTO(updatedTicket);
    }

    @Transactional(readOnly = true)
    public Ticket getTicketById(Long ticketId) throws Exception {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trouvé"));
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> getAllTickets() throws Exception {
        List<Ticket> tickets = ticketRepository.findAll();
        logger.info("Nombre de tickets récupérés: {}", tickets.size());

        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketDTOs.add(ticketMapper.toDTO(ticket));
        }
        return ticketDTOs;
    }

    @Transactional
    public void deleteTicket(Long ticketId) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trouvé"));

        ticketRepository.delete(ticket);
        logger.info("Ticket supprimé avec succès : {}", ticketId);
    }
}