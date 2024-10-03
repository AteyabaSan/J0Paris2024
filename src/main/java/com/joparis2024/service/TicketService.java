package com.joparis2024.service;

import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Event;
import com.joparis2024.model.Order;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    @Lazy // Pour éviter la dépendance circulaire
    private EventService eventService;

    @Autowired
    private OrderService orderService;

    public Ticket createTicket(TicketDTO ticketDTO) throws Exception {
        logger.info("Tentative de création d'un ticket : {}", ticketDTO);

        Event event = eventService.mapToEntity(ticketDTO.getEvent());
        Order order = orderService.mapToEntity(ticketDTO.getOrder());

        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setOrder(order);
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setQuantity(ticketDTO.getQuantity());
        ticket.setAvailable(ticketDTO.isAvailable());
        ticket.setEventDate(ticketDTO.getEventDate());

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Long ticketId, TicketDTO ticketDTO) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new Exception("Ticket non trouvé"));

        ticket.setEvent(eventService.mapToEntity(ticketDTO.getEvent()));
        ticket.setOrder(orderService.mapToEntity(ticketDTO.getOrder()));
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setQuantity(ticketDTO.getQuantity());
        ticket.setAvailable(ticketDTO.isAvailable());

        return ticketRepository.save(ticket);
    }

    public TicketDTO getTicketById(Long ticketId) throws Exception {
        return ticketRepository.findById(ticketId)
                .map(ticket -> {
                    try {
                        return mapToDTO(ticket);
                    } catch (Exception e) {
                        logger.error("Erreur lors de la conversion du ticket en DTO", e);
                        return null;
                    }
                })
                .orElseThrow(() -> new Exception("Ticket non trouvé"));
    }

    public List<TicketDTO> getAllTickets() throws Exception {
        List<Ticket> tickets = ticketRepository.findAll();
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            try {
                ticketDTOs.add(mapToDTO(ticket));
            } catch (Exception e) {
                logger.error("Erreur lors du mapping d'un ticket", e);
            }
        }
        return ticketDTOs;
    }

    public void deleteTicket(Long ticketId) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new Exception("Ticket non trouvé"));
        ticketRepository.delete(ticket);
    }

    public TicketDTO mapToDTO(Ticket ticket) throws Exception {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setEvent(eventService.mapToDTO(ticket.getEvent()));

        if (ticket.getOrder() != null) {
            ticketDTO.setOrder(orderService.mapToDTO(ticket.getOrder()));
        } else {
            logger.warn("La commande est nulle pour le ticket ID : {}", ticket.getId());
        }

        ticketDTO.setPrice(ticket.getPrice());
        ticketDTO.setQuantity(ticket.getQuantity());
        ticketDTO.setAvailable(ticket.isAvailable());
        ticketDTO.setEventDate(ticket.getEventDate());

        return ticketDTO;
    }

    public Ticket mapToEntity(TicketDTO ticketDTO) throws Exception {
        if (ticketDTO == null) {
            throw new Exception("Le TicketDTO est manquant ou invalide.");
        }

        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());

        if (ticketDTO.getEvent() != null && ticketDTO.getEvent().getId() != null) {
            Event event = eventService.mapToEntity(ticketDTO.getEvent());
            ticket.setEvent(event);
        } else {
            logger.warn("L'événement est nul ou incomplet dans TicketDTO : {}", ticketDTO);
        }

        if (ticketDTO.getOrder() != null && ticketDTO.getOrder().getId() != null) {
            Order order = orderService.mapToEntity(ticketDTO.getOrder());
            ticket.setOrder(order);
        } else {
            logger.warn("La commande est nulle ou incomplète dans TicketDTO : {}", ticketDTO);
        }

        ticket.setPrice(ticketDTO.getPrice());
        ticket.setQuantity(ticketDTO.getQuantity());
        ticket.setAvailable(ticketDTO.isAvailable());
        ticket.setEventDate(ticketDTO.getEventDate());

        return ticket;
    }

    public List<Ticket> mapToEntities(List<TicketDTO> ticketDTOs, Event event) throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        if (ticketDTOs != null) {
            for (TicketDTO ticketDTO : ticketDTOs) {
                Ticket ticket = new Ticket();
                ticket.setPrice(ticketDTO.getPrice());
                ticket.setQuantity(ticketDTO.getQuantity());
                ticket.setAvailable(ticketDTO.isAvailable());
                ticket.setOrder(orderService.mapToEntity(ticketDTO.getOrder()));
                ticket.setEvent(event);
                tickets.add(ticket);
            }
        } else {
            throw new Exception("La liste de TicketDTOs est vide");
        }
        return tickets;
    }

    public List<TicketDTO> mapToDTOs(List<Ticket> tickets) throws Exception {
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        if (tickets != null) {
            for (Ticket ticket : tickets) {
                ticketDTOs.add(mapToDTO(ticket));
            }
        } else {
            throw new Exception("La liste des tickets est vide");
        }
        return ticketDTOs;
    }

    public List<Ticket> mapToEntities(List<TicketDTO> ticketDTOs) throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        if (ticketDTOs != null) {
            for (TicketDTO ticketDTO : ticketDTOs) {
                tickets.add(mapToEntity(ticketDTO));
            }
        } else {
            throw new Exception("La liste des TicketDTOs est vide");
        }
        return tickets;
    }
}

