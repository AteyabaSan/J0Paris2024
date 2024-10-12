package com.joparis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.service.OrderManagementFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@RestController
@RequestMapping("/api/order-management")
public class OrderManagementController {

    @Autowired
    private OrderManagementFacade orderManagementFacade;

    private static final Logger logger = LoggerFactory.getLogger(OrderManagementController.class);

    // Créer une commande avec détails
    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrderWithDetails(@RequestBody OrderDTO orderDTO) {
        try {
            logger.info("Demande de création de commande avec détails pour l'utilisateur : {}", orderDTO.getUser().getId());
            OrderDTO savedOrder = orderManagementFacade.createOrderWithDetails(orderDTO);
            return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la commande : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Mettre à jour une commande avec détails
    @PutMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> updateOrderWithDetails(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
        try {
            logger.info("Demande de mise à jour de la commande ID : {}", orderId);
            OrderDTO updatedOrder = orderManagementFacade.updateOrderWithDetails(orderId, orderDTO);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la commande : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Annuler une commande
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> cancelOrderWithDetails(@PathVariable Long orderId) {
        try {
            logger.info("Demande d'annulation de la commande ID : {}", orderId);
            orderManagementFacade.cancelOrderWithDetails(orderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de l'annulation de la commande : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer une commande avec détails
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrderWithDetails(@PathVariable Long orderId) {
        try {
            logger.info("Demande de récupération des détails de la commande ID : {}", orderId);
            OrderDTO order = orderManagementFacade.getOrderWithDetails(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la commande : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Créer un ticket pour une commande
    @PostMapping("/orders/{orderId}/tickets")
    public ResponseEntity<TicketDTO> createTicketForOrder(@PathVariable Long orderId, @RequestBody TicketDTO ticketDTO) {
        try {
            logger.info("Demande de création de ticket pour la commande ID : {}", orderId);
            TicketDTO savedTicket = orderManagementFacade.createTicketForOrder(orderId, ticketDTO);
            return new ResponseEntity<>(savedTicket, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du ticket : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Mettre à jour un ticket pour une commande
    @PutMapping("/orders/{orderId}/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> updateTicketForOrder(@PathVariable Long orderId, @PathVariable Long ticketId, @RequestBody TicketDTO ticketDTO) {
        try {
            logger.info("Demande de mise à jour du ticket ID : {} pour la commande ID : {}", ticketId, orderId);
            TicketDTO updatedTicket = orderManagementFacade.updateTicketForOrder(orderId, ticketId, ticketDTO);
            return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du ticket : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer tous les tickets pour une commande
    @GetMapping("/orders/{orderId}/tickets")
    public ResponseEntity<List<TicketDTO>> getAllTicketsForOrder(@PathVariable Long orderId) {
        try {
            logger.info("Demande de récupération de tous les tickets pour la commande ID : {}", orderId);
            List<TicketDTO> tickets = orderManagementFacade.getAllTicketsForOrder(orderId);
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des tickets pour la commande : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Supprimer un ticket pour une commande
    @DeleteMapping("/orders/{orderId}/tickets/{ticketId}")
    public ResponseEntity<Void> deleteTicketForOrder(@PathVariable Long orderId, @PathVariable Long ticketId) {
        try {
            logger.info("Demande de suppression du ticket ID : {} pour la commande ID : {}", ticketId, orderId);
            orderManagementFacade.deleteTicketForOrder(orderId, ticketId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du ticket : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Assigner des rôles à un utilisateur
    @PostMapping("/users/{userId}/assign-roles")
    public ResponseEntity<Void> assignRolesToUser(@PathVariable Long userId, @RequestBody List<String> roles) {
        try {
            logger.info("Demande d'assignation des rôles à l'utilisateur ID : {}", userId);
            orderManagementFacade.assignRolesToUser(userId, roles);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de l'assignation des rôles : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Supprimer un ticket d'une commande
    @DeleteMapping("/orders/{orderId}/tickets/{ticketId}/remove")
    public ResponseEntity<Void> removeTicketFromOrder(@PathVariable Long orderId, @PathVariable Long ticketId) {
        try {
            logger.info("Demande de suppression du ticket ID : {} de la commande ID : {}", ticketId, orderId);
            orderManagementFacade.removeTicketFromOrder(orderId, ticketId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du ticket de la commande : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
