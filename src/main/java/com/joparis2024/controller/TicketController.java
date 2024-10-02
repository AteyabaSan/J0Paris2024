package com.joparis2024.controller;

import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Ticket;
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

    // Créer un ticket (CREATE)
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) {
        try {
            System.out.println("TicketDTO reçu : " + ticketDTO);

            // Appel au service pour créer le ticket
            Ticket createdTicket = ticketService.createTicket(ticketDTO);

            // Mapper le ticket créé en DTO et le renvoyer
            TicketDTO createdTicketDTO = ticketService.mapToDTO(createdTicket);
            return ResponseEntity.ok(createdTicketDTO);

        } catch (Exception e) {
            // Capture des erreurs et renvoi d'une réponse avec le message d'erreur
            System.err.println("Erreur lors de la création du ticket : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Récupérer un ticket par ID (READ)
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        try {
            System.out.println("Recherche du ticket avec ID : " + id);
            TicketDTO ticket = ticketService.getTicketById(id);
            System.out.println("Ticket trouvé : " + ticket);
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche du ticket : " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer tous les tickets (READ)
    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        try {
            System.out.println("Récupération de tous les tickets.");
            List<TicketDTO> tickets = ticketService.getAllTickets();
            System.out.println("Tickets récupérés : " + tickets);
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des tickets : " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Mettre à jour un ticket (UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        try {
            System.out.println("Mise à jour du ticket avec ID : " + id + ", nouvelles informations : " + ticketDTO);
            TicketDTO updatedTicket = ticketService.mapToDTO(ticketService.updateTicket(id, ticketDTO));
            System.out.println("Ticket mis à jour avec succès : " + updatedTicket);
            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du ticket : " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un ticket (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        try {
            System.out.println("Suppression du ticket avec ID : " + id);
            ticketService.deleteTicket(id);
            System.out.println("Ticket supprimé avec succès.");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression du ticket : " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
