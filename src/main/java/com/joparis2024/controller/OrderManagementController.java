package com.joparis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.User;
import com.joparis2024.model.UserRole;
import com.joparis2024.repository.UserRepository;
import com.joparis2024.repository.UserRoleRepository;
import com.joparis2024.service.OrderManagementFacade;

//import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/order-management")
public class OrderManagementController {

    @Autowired
    private OrderManagementFacade orderManagementFacade;
    
    @Autowired
    private UserRepository userRepository;
    

    @Autowired
    private UserRoleRepository userRoleRepository;

   
    

    private static final Logger logger = LoggerFactory.getLogger(OrderManagementController.class);
   
    @PostMapping("/orders/create")
    public ResponseEntity<OrderDTO> createOrderWithDetails(@RequestBody OrderDTO orderDTO) {
        try {
            logger.info("Demande de création de commande avec détails pour l'utilisateur : {}", orderDTO.getUser().getId());

            // Vérifier si l'utilisateur existe déjà par email
            Optional<User> existingUser = userRepository.findByEmail(orderDTO.getUser().getEmail());
            if (!existingUser.isPresent()) {
                throw new Exception("Utilisateur non trouvé. L'utilisateur doit être inscrit avant de passer une commande.");
            }

            User utilisateur = existingUser.get();
            logger.info("L'utilisateur existe déjà avec l'email : {}", utilisateur.getEmail());

            // Assigner ou vérifier les rôles via UserRole
            List<UserRole> userRoles = userRoleRepository.findByUser(utilisateur);
            if (userRoles == null || userRoles.isEmpty()) {
                throw new Exception("L'utilisateur n'a aucun rôle assigné.");
            }

            // Assurer que l'utilisateur a bien le rôle 'USER'
            boolean hasUserRole = false;
            for (UserRole userRole : userRoles) {
                if (userRole.getRole().getName().equals("USER")) {
                    hasUserRole = true;
                    break;
                }
            }

            if (!hasUserRole) {
                throw new Exception("L'utilisateur doit avoir au moins le rôle 'USER'.");
            }

            // Créer la commande
            orderDTO.getUser().setId(utilisateur.getId());
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

            // Vérifier si l'utilisateur existe déjà dans la base de données
            Optional<User> existingUserOptional = userRepository.findByEmail(orderDTO.getUser().getEmail());
            if (!existingUserOptional.isPresent()) {
                throw new Exception("Utilisateur non trouvé.");
            }

            User existingUser = existingUserOptional.get();

            // Vérifier si le champ 'username' est manquant ou vide, et le compléter si nécessaire
            if (orderDTO.getUser().getUsername() == null || orderDTO.getUser().getUsername().isEmpty()) {
                logger.info("Le champ 'username' est manquant. Utilisation du 'username' existant pour l'utilisateur : {}", existingUser.getUsername());
                orderDTO.getUser().setUsername(existingUser.getUsername()); // Compléter avec l'ancien username
            }

            // Vérifier si le champ 'enabled' est manquant et le compléter si nécessaire
            if (orderDTO.getUser().getEnabled() == null) {
                logger.info("Le champ 'enabled' est manquant. Utilisation de la valeur existante pour l'utilisateur : {}", existingUser.getEnabled());
                orderDTO.getUser().setEnabled(existingUser.getEnabled()); // Compléter avec l'ancien statut enabled
            }

            // Compléter d'autres champs sensibles si besoin

            // Appeler la méthode pour mettre à jour la commande avec les détails
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
    public ResponseEntity<?> createTicketForOrder(@PathVariable Long orderId, @RequestBody TicketDTO ticketDTO) {
        logger.info("Demande de création de ticket pour la commande ID : {}", orderId);

        // Log des champs du TicketDTO pour vérification
        logger.info("TicketDTO après désérialisation - Event ID: {}, Order ID: {}, Price: {}, Quantity: {}, Available: {}, EventDate: {}", 
                    ticketDTO.getEvent() != null ? ticketDTO.getEvent().getId() : null,
                    ticketDTO.getOrder() != null ? ticketDTO.getOrder().getId() : null,
                    ticketDTO.getPrice(),
                    ticketDTO.getQuantity(),
                    ticketDTO.isAvailable(),
                    ticketDTO.getEventDate());

        // Vérification de la quantité
        if (ticketDTO.getQuantity() == null || ticketDTO.getQuantity() <= 0) {
            logger.error("Quantité invalide après désérialisation : {}", ticketDTO.getQuantity());
            return new ResponseEntity<>("La quantité de tickets doit être supérieure à 0.", HttpStatus.BAD_REQUEST);
        }

        try {
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
