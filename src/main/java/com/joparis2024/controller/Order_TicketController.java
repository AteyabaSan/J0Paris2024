package com.joparis2024.controller;

import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.service.Order_TicketService;

import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/order-tickets")
public class Order_TicketController {

    @Autowired
    private Order_TicketService orderTicketService;

    private static final Logger logger = LoggerFactory.getLogger(Order_TicketController.class);

    // Créer une association Order_Ticket
    @PostMapping
    public ResponseEntity<Order_TicketDTO> createOrderTicket(@RequestBody Order_TicketDTO orderTicketDTO) {
        try {
            logger.info("Requête pour créer une association Order_Ticket entre Order et Ticket");
            Order_TicketDTO createdOrderTicket = orderTicketService.createOrderTicket(orderTicketDTO);
            logger.info("Association Order_Ticket créée avec succès");
            return new ResponseEntity<>(createdOrderTicket, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de la création de l'association Order_Ticket : " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création de l'association Order_Ticket", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer toutes les associations Order_Ticket pour une commande
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Order_TicketDTO>> getOrderTicketsByOrder(@PathVariable Long orderId) {
        try {
            logger.info("Requête pour récupérer toutes les associations Order_Ticket pour la commande avec ID : " + orderId);
            List<Order_TicketDTO> orderTickets = orderTicketService.getOrderTicketsByOrder(orderId);
            if (orderTickets.isEmpty()) {
                logger.warn("Aucune association Order_Ticket trouvée pour la commande avec ID : " + orderId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            logger.info("Associations Order_Ticket récupérées avec succès pour la commande avec ID : " + orderId);
            return new ResponseEntity<>(orderTickets, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des associations Order_Ticket pour la commande avec ID : " + orderId, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer une association Order_Ticket par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Order_TicketDTO> getOrderTicketById(@PathVariable Long id) {
        try {
            logger.info("Requête pour récupérer l'association Order_Ticket avec ID : " + id);
            Order_TicketDTO orderTicketDTO = orderTicketService.getOrderTicketById(id);
            logger.info("Association Order_Ticket récupérée avec succès");
            return new ResponseEntity<>(orderTicketDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.warn("Association Order_Ticket avec ID : " + id + " non trouvée");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'association Order_Ticket avec ID : " + id, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Supprimer une association Order_Ticket
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderTicket(@PathVariable Long id) {
        try {
            logger.info("Requête pour supprimer l'association Order_Ticket avec ID : " + id);
            orderTicketService.deleteOrderTicket(id);
            logger.info("Association Order_Ticket supprimée avec succès");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'association Order_Ticket avec ID : " + id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
