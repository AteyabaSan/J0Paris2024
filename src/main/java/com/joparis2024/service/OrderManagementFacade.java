package com.joparis2024.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.EventMapper;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.model.Offer;
import com.joparis2024.model.Order;
import com.joparis2024.model.Ticket;
import com.joparis2024.model.User;
import com.joparis2024.model.UserRole;
import com.joparis2024.repository.OfferRepository;
import com.joparis2024.repository.OrderRepository;
import com.joparis2024.repository.TicketRepository;
import com.joparis2024.repository.UserRepository;
import com.joparis2024.repository.UserRoleRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class OrderManagementFacade {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private Order_TicketService orderTicketService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TicketRepository ticketRepository; 
    
    @Autowired
    private EventMapper eventMapper;
      
    @Autowired
    public OrderMapper orderMapper; 
    
    @Autowired
    private OrderDetailService orderDetailService;
    
    @Autowired
    private OfferRepository offerRepository; 
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private EmailService emailService;
    
    private static final Logger logger = LoggerFactory.getLogger(OrderManagementFacade.class);

    
 // Méthode complexe : Créer une commande avec tickets et gérer les rôles d'utilisateur
    @Transactional
    public OrderDTO createOrderWithDetails(OrderDTO orderDTO) throws Exception {
        logger.info("Création de la commande avec tickets pour l'utilisateur : {}", orderDTO.getUser().getId());

        if (orderDTO.getUser() == null || orderDTO.getUser().getEmail() == null || orderDTO.getUser().getEmail().isEmpty()) {
            throw new Exception("Email non valide");
        }

        // Vérifier si l'utilisateur existe dans la base de données
        Optional<User> existingUser = userRepository.findByEmail(orderDTO.getUser().getEmail());
        if (!existingUser.isPresent()) {
            throw new Exception("Utilisateur non trouvé.");
        }

        User user = existingUser.get();
        orderDTO.getUser().setId(user.getId());

        // Vérification des rôles via UserRole
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        logger.info("Rôles récupérés pour l'utilisateur {} : {}", user.getUsername(), userRoles);

        if (userRoles.isEmpty()) {
            throw new Exception("L'utilisateur n'a aucun rôle assigné.");
        }

        boolean hasRequiredRole = userRoles.stream().anyMatch(ur -> ur.getRole().getName().equals("USER") || ur.getRole().getName().equals("ADMIN"));
        if (!hasRequiredRole) {
            throw new Exception("L'utilisateur doit avoir au moins le rôle 'USER' ou 'ADMIN'.");
        }

        // Vérifier et définir la date de commande
        if (orderDTO.getOrderDate() == null) {
            orderDTO.setOrderDate(LocalDateTime.now());
        }

        // Vérification et affectation du statut si manquant
        if (orderDTO.getStatus() == null || orderDTO.getStatus().isEmpty()) {
            orderDTO.setStatus("EN_COURS");
        }

        double totalAmount = 0.0;

        // Créer la commande
        OrderDTO savedOrderDTO = orderService.createOrder(orderDTO);

        // Associer les tickets
        if (orderDTO.getTickets() != null && !orderDTO.getTickets().isEmpty()) {
            List<TicketDTO> ticketDTOs = new ArrayList<>();
            for (TicketDTO ticketDTO : orderDTO.getTickets()) {
                if (ticketDTO.getQuantity() <= 0) {
                    throw new Exception("La quantité doit être supérieure à 0.");
                }

                // Vérifier l'offre et s'assurer qu'elle n'est pas nulle
                if (ticketDTO.getOfferId() == null) {
                    throw new Exception("L'offre doit être spécifiée pour chaque ticket.");
                }
                Offer offer = offerRepository.findById(ticketDTO.getOfferId())
                        .orElseThrow(() -> new Exception("Offre non trouvée."));

                // Récupérer le ticket
                Ticket ticket = ticketRepository.findById(ticketDTO.getId())
                        .orElseThrow(() -> new Exception("Ticket non trouvé."));

                ticketDTO.setPrice(ticket.getPrice());
                ticketDTO.setAvailable(ticket.isAvailable());
                ticketDTO.setEvent(eventMapper.toDTO(ticket.getEvent()));

                totalAmount += ticket.getPrice() * ticketDTO.getQuantity();

                Order_TicketDTO orderTicketDTO = new Order_TicketDTO(savedOrderDTO.getId(), ticketDTO.getId(), ticketDTO.getQuantity(), offer.getId());
                orderTicketService.createOrderTicket(orderTicketDTO);

                ticketDTOs.add(ticketDTO);
            }
            savedOrderDTO.setTickets(ticketDTOs);
        }

        logger.info("Montant total calculé : {}", totalAmount);
        savedOrderDTO.setTotalAmount(totalAmount);
        orderService.updateOrder(savedOrderDTO.getId(), savedOrderDTO);

        return savedOrderDTO;
    }


    // Méthode complexe : Mise à jour d'une commande avec détails et tickets associés
    @Transactional
    public OrderDTO updateOrderWithDetails(Long orderId, OrderDTO orderDTO) throws Exception {
        logger.info("Mise à jour de la commande avec ID: {}", orderId);

        // Compléter le statut si manquant
        if (orderDTO.getStatus() == null || orderDTO.getStatus().isEmpty()) {
            orderDTO.setStatus("EN_COURS"); // Statut par défaut si manquant
        }

        // Mise à jour des informations utilisateur et des rôles si nécessaire
        userService.updateUserByEmail(orderDTO.getUser().getEmail(), orderDTO.getUser());

        // Recalculer le montant total
        double totalAmount = 0.0;

        // Mise à jour des tickets associés via Order_TicketService
        if (orderDTO.getTickets() != null && !orderDTO.getTickets().isEmpty()) {
            List<TicketDTO> ticketDTOs = new ArrayList<>();
            for (TicketDTO ticketDTO : orderDTO.getTickets()) {
                // Vérification de la quantité
                if (ticketDTO.getQuantity() == null || ticketDTO.getQuantity() <= 0) {
                    throw new Exception("La quantité de tickets doit être supérieure à 0.");
                }

                // Vérifier l'offre et s'assurer qu'elle n'est pas nulle
                if (ticketDTO.getOfferId() == null) {
                    throw new Exception("L'offre doit être spécifiée pour chaque ticket.");
                }
                Offer offer = offerRepository.findById(ticketDTO.getOfferId())
                        .orElseThrow(() -> new Exception("Offre non trouvée."));

                // Charger les informations complètes du ticket
                Ticket ticket = ticketRepository.findById(ticketDTO.getId())
                              .orElseThrow(() -> new Exception("Ticket non trouvé."));

                // Compléter le TicketDTO avec les données récupérées
                ticketDTO.setPrice(ticket.getPrice());
                ticketDTO.setAvailable(ticket.isAvailable());
                ticketDTO.setEvent(eventMapper.toDTO(ticket.getEvent())); // Mapper l'Event

                // Calculer le montant total basé sur les tickets
                totalAmount += ticket.getPrice() * ticketDTO.getQuantity();

                // Assigner l'offre à l'objet TicketDTO (utilisation correcte de l'objet Offer)
                ticketDTO.setOfferId(offer.getId()); // Assigner l'offre récupérée

                // Créer l'association entre commande et ticket dans Order_Ticket
                Order_TicketDTO orderTicketDTO = new Order_TicketDTO(
                    orderId,         // ID de la commande
                    ticketDTO.getId(), // ID du ticket
                    ticketDTO.getQuantity(), // Quantité de tickets
                    offer.getId() // ID de l'offre liée au ticket
                );
                orderTicketService.createOrderTicket(orderTicketDTO);

                // Ajouter le ticket à la liste des tickets
                ticketDTOs.add(ticketDTO);
            }
            orderDTO.setTickets(ticketDTOs); // Mettre à jour les tickets dans la commande
        }

        // Définir le montant total
        orderDTO.setTotalAmount(totalAmount);

        // Mise à jour de la commande avec le montant total recalculé
        OrderDTO updatedOrderDTO = orderService.updateOrder(orderId, orderDTO);

        return updatedOrderDTO;
    }


    // Méthode complexe : Annuler une commande avec les tickets associés
    @Transactional
    public void cancelOrderWithDetails(Long orderId) throws Exception {
        logger.info("Annulation de la commande avec ID: {}", orderId);

        // Suppression des tickets associés via Order_TicketService
        List<Order_TicketDTO> orderTickets = orderTicketService.getOrderTicketsByOrder(orderId);
        for (Order_TicketDTO orderTicketDTO : orderTickets) {
            orderTicketService.deleteOrderTicket(orderTicketDTO.getId());
        }

        // Annulation de la commande
        orderService.cancelOrder(orderId);
    }

    // Méthode complexe : Récupérer une commande avec tous les détails
    @Transactional(readOnly = true)
    public OrderDTO getOrderWithDetails(Long orderId) throws Exception {
        logger.info("Récupération de la commande avec détails pour ID: {}", orderId);
        
        OrderDTO orderDTO = orderDetailService.getOrderWithDetailsById(orderId);

        // Récupérer les tickets associés et leur ajouter les détails
        List<Order_TicketDTO> orderTicketDTOs = orderTicketService.getOrderTicketsByOrder(orderId);
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        
        for (Order_TicketDTO orderTicketDTO : orderTicketDTOs) {
            Ticket ticket = ticketRepository.findById(orderTicketDTO.getTicketId())
                            .orElseThrow(() -> new Exception("Ticket non trouvé."));
            
            TicketDTO ticketDTO = new TicketDTO();
            ticketDTO.setId(ticket.getId());
            ticketDTO.setPrice(ticket.getPrice());
            ticketDTO.setAvailable(ticket.isAvailable());
            ticketDTO.setEvent(eventMapper.toDTO(ticket.getEvent())); // Mapper l'Event

            ticketDTOs.add(ticketDTO); // Ajouter à la liste des tickets
        }
        
        orderDTO.setTickets(ticketDTOs); // Mettre à jour les tickets dans la commande

        return orderDTO;
    }  
    
    public Order getOrderEntityById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée pour l'ID : " + orderId));
    }
    
    
 // Méthode pour récupérer la commande par sessionId Stripe
    public OrderDTO getOrderBySessionId(String sessionId) throws Exception {
        OrderDTO orderDTO = orderService.findByStripeSessionId(sessionId); // Utilisation de ta méthode existante
        if (orderDTO == null) {
            throw new EntityNotFoundException("Commande non trouvée pour la session : " + sessionId);
        }
        return orderDTO;  // Retourne directement le DTO
    }
    
    // Méthode pour envoyer des tickets après une commande confirmée
    public void sendTicketsForOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);  // Convertir DTO en entité
        emailService.sendTicket(order);  // Utiliser la méthode existante dans EmailService
    }
}
