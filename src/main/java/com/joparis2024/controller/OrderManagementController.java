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

    // Créer une commande avec détails et tickets
    @PostMapping("/orders/create")
    public ResponseEntity<OrderDTO> createOrderWithDetails(@RequestBody OrderDTO orderDTO) {
        try {
            logger.info("Demande de création de commande avec détails pour l'utilisateur : {}", orderDTO.getUser().getId());

            // Vérification de l'utilisateur par email
            Optional<User> existingUser = userRepository.findByEmail(orderDTO.getUser().getEmail());
            if (!existingUser.isPresent()) {
                logger.error("Utilisateur non trouvé avec l'email : {}", orderDTO.getUser().getEmail());
                throw new Exception("Utilisateur non trouvé. L'utilisateur doit être inscrit avant de passer une commande.");
            }

            User utilisateur = existingUser.get();
            logger.info("L'utilisateur existe avec l'email : {}", utilisateur.getEmail());

            // Vérification des rôles de l'utilisateur
            List<UserRole> userRoles = userRoleRepository.findByUser(utilisateur);
            if (userRoles == null || userRoles.isEmpty()) {
                logger.error("Aucun rôle assigné pour l'utilisateur : {}", utilisateur.getUsername());
                throw new Exception("L'utilisateur n'a aucun rôle assigné.");
            }

            // Vérification que l'utilisateur a le rôle requis
            boolean hasValidRole = userRoles.stream().anyMatch(ur -> ur.getRole().getName().equals("USER") || ur.getRole().getName().equals("ADMIN"));
            if (!hasValidRole) {
                logger.error("L'utilisateur {} n'a pas de rôle suffisant (USER ou ADMIN).", utilisateur.getUsername());
                throw new Exception("L'utilisateur doit avoir au moins le rôle 'USER' ou 'ADMIN'.");
            }

            // Log des tickets et validation des quantités et offres
            if (orderDTO.getTickets() != null && !orderDTO.getTickets().isEmpty()) {
                for (TicketDTO ticketDTO : orderDTO.getTickets()) {
                    logger.info("TicketDTO: ID: {}, Quantity: {}, OfferID: {}", ticketDTO.getId(), ticketDTO.getQuantity(), ticketDTO.getOfferId());
                    if (ticketDTO.getQuantity() == null || ticketDTO.getQuantity() <= 0) {
                        logger.error("Quantité de tickets invalide pour le ticket ID: {}", ticketDTO.getId());
                        throw new Exception("La quantité de tickets doit être supérieure à 0.");
                    }
                    if (ticketDTO.getOfferId() == null) {
                        logger.error("Offre non spécifiée pour le ticket ID: {}", ticketDTO.getId());
                        throw new Exception("Une offre doit être spécifiée pour chaque ticket.");
                    }
                }
            }

            // Créer la commande via le facade
            orderDTO.getUser().setId(utilisateur.getId());
            OrderDTO savedOrder = orderManagementFacade.createOrderWithDetails(orderDTO);

            // Retourner la commande créée avec le statut 201 (Created)
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
}
