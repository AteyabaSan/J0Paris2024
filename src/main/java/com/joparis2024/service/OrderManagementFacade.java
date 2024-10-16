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
import com.joparis2024.dto.UserDTO;
import com.joparis2024.mapper.EventMapper;
import com.joparis2024.model.Offer;
import com.joparis2024.model.Ticket;
import com.joparis2024.model.User;
import com.joparis2024.model.UserRole;
import com.joparis2024.repository.OfferRepository;
import com.joparis2024.repository.TicketRepository;
import com.joparis2024.repository.UserRepository;
import com.joparis2024.repository.UserRoleRepository;


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
    private OrderDetailService orderDetailService;
    
    @Autowired
    private OfferRepository offerRepository; 

    private static final Logger logger = LoggerFactory.getLogger(OrderManagementFacade.class);

    
 // Méthode complexe : Créer une commande avec tickets et gérer les rôles d'utilisateur
    @Transactional
    public OrderDTO createOrderWithDetails(OrderDTO orderDTO) throws Exception {
        logger.info("Création de la commande avec tickets pour l'utilisateur : {}", orderDTO.getUser().getId());

        // Validation de l'email de l'utilisateur
        if (orderDTO.getUser() == null || orderDTO.getUser().getEmail() == null || orderDTO.getUser().getEmail().isEmpty()) {
            throw new Exception("Email non valide");
        }

        // Vérifier si l'utilisateur existe dans la base de données
        Optional<User> existingUser = userRepository.findByEmail(orderDTO.getUser().getEmail());
        if (!existingUser.isPresent()) {
            throw new Exception("Utilisateur non trouvé. L'utilisateur doit être inscrit avant de passer une commande.");
        }

        User user = existingUser.get();
        orderDTO.getUser().setId(user.getId()); // Utiliser l'utilisateur récupéré dans la commande

        // Vérification des rôles via UserRole
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        logger.info("Rôles récupérés pour l'utilisateur {} : {}", user.getUsername(), userRoles);

        if (userRoles == null || userRoles.isEmpty()) {
            throw new Exception("L'utilisateur n'a aucun rôle assigné.");
        }

        // Assurer que l'utilisateur a au moins le rôle 'USER' ou 'ADMIN'
        boolean hasRequiredRole = false;
        for (UserRole userRole : userRoles) {
            logger.info("Vérification du rôle : {}", userRole.getRole().getName());
            if (userRole.getRole().getName().equals("USER") || userRole.getRole().getName().equals("ADMIN")) {
                hasRequiredRole = true;
                break;
            }
        }

        if (!hasRequiredRole) {
            throw new Exception("L'utilisateur doit avoir au moins le rôle 'USER' ou 'ADMIN'.");
        }

        // Avant de créer la commande, vérifier et définir orderDate
        if (orderDTO.getOrderDate() == null) {
            orderDTO.setOrderDate(LocalDateTime.now());
        }

        // Initialiser le statut de la commande
        orderDTO.setStatus("EN_COURS");

        // Calculer le montant total avant la sauvegarde
        double totalAmount = 0.0;

        // Créer la commande sans tickets (le montant sera calculé ensuite)
        OrderDTO savedOrderDTO = orderService.createOrder(orderDTO);

        // Associer les tickets à la commande via Order_TicketService et récupérer leurs informations complètes
        if (orderDTO.getTickets() != null && !orderDTO.getTickets().isEmpty()) {
            List<TicketDTO> ticketDTOs = new ArrayList<>();
            for (TicketDTO ticketDTO : orderDTO.getTickets()) {
                // Vérification de la quantité
                if (ticketDTO.getQuantity() == null || ticketDTO.getQuantity() <= 0) {
                    throw new Exception("La quantité de tickets doit être supérieure à 0.");
                }

                // Vérification de l'offre spécifiée
                if (ticketDTO.getOfferId() == null) {
                    throw new Exception("Une offre doit être spécifiée pour le ticket.");
                }

                // Charger l'offre à partir de son ID
                Offer offer = offerRepository.findById(ticketDTO.getOfferId())
                    .orElseThrow(() -> new Exception("Offre non trouvée."));

                // Charger les informations complètes du ticket
                Ticket ticket = ticketRepository.findById(ticketDTO.getId())
                    .orElseThrow(() -> new Exception("Ticket non trouvé."));

                // Compléter le TicketDTO avec les données récupérées
                ticketDTO.setPrice(ticket.getPrice());
                ticketDTO.setAvailable(ticket.isAvailable());
                ticketDTO.setEvent(eventMapper.toDTO(ticket.getEvent())); // Utiliser le EventMapper pour convertir l'Event en EventDTO

                // Ajouter au montant total (prix du ticket * quantité)
                totalAmount += ticket.getPrice() * ticketDTO.getQuantity();

                // Créer l'association entre commande et ticket dans Order_Ticket, avec l'offre spécifiée
                Order_TicketDTO orderTicketDTO = new Order_TicketDTO(
                    savedOrderDTO.getId(),  // ID de la commande
                    ticketDTO.getId(),      // ID du ticket
                    ticketDTO.getQuantity(),// Quantité de tickets
                    offer.getId()           // ID de l'offre
                );
                orderTicketService.createOrderTicket(orderTicketDTO);

                // Ajouter le ticket à la liste des tickets
                ticketDTOs.add(ticketDTO);
            }
            savedOrderDTO.setTickets(ticketDTOs); // Mettre à jour les tickets dans la commande
        }

        // Mettre à jour le montant total dans la commande
        logger.info("Montant total calculé : {}", totalAmount);
        savedOrderDTO.setTotalAmount(totalAmount);

        // Sauvegarder la commande avec le montant total calculé
        orderService.updateOrder(savedOrderDTO.getId(), savedOrderDTO);

        // Récupérer les informations complètes de l'utilisateur pour le retourner
        List<String> roles = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            roles.add(userRole.getRole().getName());
        }

        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), roles, 
                                      user.getEnabled(), user.getPhoneNumber(), null);
        savedOrderDTO.setUser(userDTO); // Mettre à jour l'utilisateur dans la commande

        return savedOrderDTO;
    }
    // Méthode complexe : Mise à jour d'une commande avec détails et tickets associés
    @Transactional
    public OrderDTO updateOrderWithDetails(Long orderId, OrderDTO orderDTO) throws Exception {
        logger.info("Mise à jour de la commande avec ID: {}", orderId);

        // Mise à jour des informations utilisateur et des rôles si nécessaire
        userService.updateUserByEmail(orderDTO.getUser().getEmail(), orderDTO.getUser());

        // Mise à jour de la commande
        OrderDTO updatedOrderDTO = orderService.updateOrder(orderId, orderDTO);

        // Mise à jour des tickets associés via Order_TicketService
        if (orderDTO.getTickets() != null && !orderDTO.getTickets().isEmpty()) {
            List<TicketDTO> ticketDTOs = new ArrayList<>();
            for (TicketDTO ticketDTO : orderDTO.getTickets()) {
                // Vérification de la quantité
                if (ticketDTO.getQuantity() == null || ticketDTO.getQuantity() <= 0) {
                    throw new Exception("La quantité de tickets doit être supérieure à 0.");
                }

                // Charger les informations complètes du ticket
                Ticket ticket = ticketRepository.findById(ticketDTO.getId())
                              .orElseThrow(() -> new Exception("Ticket non trouvé."));

                // Compléter le TicketDTO avec les données récupérées
                ticketDTO.setPrice(ticket.getPrice());
                ticketDTO.setAvailable(ticket.isAvailable());
                ticketDTO.setEvent(eventMapper.toDTO(ticket.getEvent())); // Mapper l'Event

                // Créer l'association entre commande et ticket dans Order_Ticket
                Order_TicketDTO orderTicketDTO = new Order_TicketDTO(
                    orderId,         // ID de la commande
                    ticketDTO.getId(), // ID du ticket
                    ticketDTO.getQuantity() // Quantité de tickets
                );
                orderTicketService.createOrderTicket(orderTicketDTO);

                // Ajouter le ticket à la liste des tickets
                ticketDTOs.add(ticketDTO);
            }
            updatedOrderDTO.setTickets(ticketDTOs); // Mettre à jour les tickets dans la commande
        }

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
}
