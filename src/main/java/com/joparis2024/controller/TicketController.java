package com.joparis2024.controller;

import com.joparis2024.dto.TicketDTO;
import com.joparis2024.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;



@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Créer un ticket
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) {
        logger.info("Requête reçue pour créer un nouveau ticket");
        try {
            TicketDTO createdTicket = ticketService.createTicket(ticketDTO);
            return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du ticket", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer un ticket par ID
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        logger.info("Requête reçue pour récupérer le ticket avec l'ID: {}", id);
        try {
            TicketDTO ticketDTO = ticketService.getTicketById(id);
            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du ticket", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer tous les tickets
    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        logger.info("Requête reçue pour récupérer tous les tickets");
        try {
            List<TicketDTO> tickets = ticketService.getAllTickets();
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des tickets", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Mettre à jour un ticket
    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        logger.info("Requête reçue pour mettre à jour le ticket avec l'ID: {}", id);
        try {
            TicketDTO updatedTicket = ticketService.updateTicket(id, ticketDTO);
            return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du ticket", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer un ticket
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        logger.info("Requête reçue pour supprimer le ticket avec l'ID: {}", id);
        try {
            ticketService.deleteTicket(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du ticket", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
