package com.joparis2024.controller;

import com.joparis2024.dto.TicketDTO;
import com.joparis2024.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // Récupérer tous les tickets
    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        System.out.println("Tentative de récupération de tous les tickets");
        List<TicketDTO> tickets = ticketService.getAllTickets();
        System.out.println("Tickets récupérés : " + tickets.size());
        return ResponseEntity.ok(tickets);
    }

    // Créer un ticket
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) {
        try {
            System.out.println("Tentative de création d'un ticket pour l'événement : " + ticketDTO.getEvent().getId());
            TicketDTO createdTicket = ticketService.mapToDTO(ticketService.createTicket(ticketDTO));
            System.out.println("Ticket créé avec succès : " + createdTicket.getId());
            return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du ticket : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Mettre à jour un ticket
    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        try {
            System.out.println("Tentative de mise à jour du ticket avec ID : " + id);
            TicketDTO updatedTicket = ticketService.mapToDTO(ticketService.updateTicket(id, ticketDTO));
            System.out.println("Ticket mis à jour avec succès : " + updatedTicket.getId());
            return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du ticket : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
