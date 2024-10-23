package com.joparis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.OfferDTO;
import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.model.Order;
import com.joparis2024.model.User;
import com.joparis2024.model.UserRole;
import com.joparis2024.repository.UserRepository;
import com.joparis2024.repository.UserRoleRepository;
import com.joparis2024.service.EmailService;
import com.joparis2024.service.OrderManagementFacade;
import com.joparis2024.service.OrderService;
import com.joparis2024.service.PaymentManagementFacade;
import com.joparis2024.service.StripeService;

import jakarta.servlet.http.HttpSession;

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
    
    @Autowired
    private PaymentManagementFacade paymentManagementFacade;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private OrderMapper orderMapper;
  
    @Autowired
    private OrderService orderService;  // Injection du service

    @Autowired
    private StripeService stripeService; // Intégration de Stripe

    private static final Logger logger = LoggerFactory.getLogger(OrderManagementController.class);

    // Créer une commande avec détails et tickets
    @PostMapping("/api/orders/create-with-details")
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
    
 // Nouvelle méthode pour créer une session Stripe
    @PostMapping("/payment/create-session")
    public ResponseEntity<String> createStripeSession(@RequestParam("orderId") Long orderId) {
        try {
            // Récupérer la commande par ID
            Order order = orderManagementFacade.getOrderEntityById(orderId);
            
            // Créer une session Stripe pour cette commande
            String sessionId = stripeService.createStripePaymentSession(order);

            // Retourner l'ID de session Stripe
            return new ResponseEntity<>(sessionId, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la session Stripe : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @GetMapping("/order-recap")
    public String getOrderRecap(Model model, HttpSession session) {
        EventDTO selectedEvent = (EventDTO) session.getAttribute("selectedEvent");
        OfferDTO selectedOffer = (OfferDTO) session.getAttribute("selectedOffer");

        if (selectedEvent == null || selectedOffer == null) {
            return "error";  // Retourner une vue d'erreur si les données sont manquantes
        }

        // Récupérer les informations de l'événement et de l'offre
        model.addAttribute("event", selectedEvent);
        model.addAttribute("offer", selectedOffer);

        // Calculer le total en fonction des tickets de l'événement et de l'offre
        double totalAmount = orderService.calculateTotalPrice(selectedEvent.getTickets(), selectedOffer);  // Assurez-vous que la méthode prend une liste de tickets
        model.addAttribute("totalAmount", totalAmount);

        // Afficher la vue de récapitulatif
        return "order-recap";
    }

    
    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam("session_id") String sessionId, Model model) {
        try {
            // Confirmer le paiement via Stripe
            paymentManagementFacade.confirmPayment(sessionId);

            // Récupérer la commande liée à la session
            OrderDTO order = orderManagementFacade.getOrderBySessionId(sessionId);

            // Envoyer les billets par email
            emailService.sendTicket(orderMapper.toEntity(order));

            model.addAttribute("message", "Paiement confirmé et tickets envoyés !");
            return "paymentConfirmation";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la confirmation du paiement.");
            return "error";
        }
    }
    
  
}
