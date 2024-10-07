package com.joparis2024.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joparis2024.model.Event;
import com.joparis2024.model.Ticket;

@Service
public class EventTicketFacade {

    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    // Méthode pour assigner des tickets à un événement
    public void assignTicketsToEvent(Long eventId, List<Long> ticketIds) throws Exception {
        // Récupérer l'événement
        Event event = eventService.findById(eventId);
        
        // Récupérer les tickets par leurs IDs et les associer à l'événement
        List<Ticket> tickets = new ArrayList<>();
        for (Long ticketId : ticketIds) {
            Ticket ticket = ticketService.getTicketById(ticketId);  // Utilise TicketService pour obtenir le ticket
            ticket.setEvent(event);  // Associe l'événement au ticket
            tickets.add(ticket);
        }

        // Sauvegarder les tickets via TicketService (pas besoin d'utiliser ticketMapper ici)
        for (Ticket ticket : tickets) {
            ticketService.updateTicket(ticket.getId(), ticketService.convertToDTO(ticket));  // Convertit en DTO via le service
        }
    }

    // Récupérer les tickets associés à un événement
    public List<Ticket> getTicketsForEvent(Long eventId) throws Exception {
        Event event = eventService.findById(eventId);
        return event.getTickets();  // Les tickets sont déjà liés dans l'entité Event
    }
}
