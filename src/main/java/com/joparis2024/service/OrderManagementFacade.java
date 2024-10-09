package com.joparis2024.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.dto.UserDTO;
import com.joparis2024.mapper.UserMapper;
import com.joparis2024.model.Role;
import com.joparis2024.model.User;


@Service
public class OrderManagementFacade {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Order_TicketService orderTicketService;

    @Autowired
    private OrderDetailService orderDetailService;

    private static final Logger logger = LoggerFactory.getLogger(OrderManagementFacade.class);

    // Méthode complexe : Créer une commande avec tickets et gérer les rôles d'utilisateur
    @Transactional
    public OrderDTO createOrderWithDetails(OrderDTO orderDTO) throws Exception {
        logger.info("Création de la commande avec tickets pour l'utilisateur : {}", orderDTO.getUser().getId());

        // Utilisation de UserService pour récupérer l'utilisateur
        User user = userService.findById(orderDTO.getUser().getId());
        UserDTO userDTO = userService.createUser(userMapper.toDTO(user)); // Crée ou récupère l'utilisateur
        orderDTO.setUser(userDTO);

        // Gestion des rôles utilisateur si nécessaire via la façade
        if (orderDTO.getUser().getRoles() != null && !orderDTO.getUser().getRoles().isEmpty()) {
            assignRolesToUser(orderDTO.getUser().getId(), orderDTO.getUser().getRoles()); // Appel de la méthode dans la façade
        }

        // Création de la commande via OrderService
        OrderDTO savedOrderDTO = orderService.createOrder(orderDTO);

        // Associer des tickets à la commande via Order_TicketService
        if (orderDTO.getTickets() != null && !orderDTO.getTickets().isEmpty()) {
            for (TicketDTO ticketDTO : orderDTO.getTickets()) {
                orderTicketService.createOrderTicket(new Order_TicketDTO(savedOrderDTO.getId(), ticketDTO.getId()));
            }
        }

        return savedOrderDTO;
    }

    // Méthode complexe : Mise à jour d'une commande avec détails et tickets associés
    @Transactional
    public OrderDTO updateOrderWithDetails(Long orderId, OrderDTO orderDTO) throws Exception {
        logger.info("Mise à jour de la commande avec ID: {}", orderId);

        // Mise à jour des informations utilisateur et des rôles si nécessaire
        userService.updateUserByEmail(orderDTO.getUser().getEmail(), orderDTO.getUser()); // Tu n'as pas besoin de récupérer le UserDTO ici

        // Mise à jour de la commande
        OrderDTO updatedOrderDTO = orderService.updateOrder(orderId, orderDTO);

        // Mise à jour des tickets associés via Order_TicketService
        if (orderDTO.getTickets() != null && !orderDTO.getTickets().isEmpty()) {
            for (TicketDTO ticketDTO : orderDTO.getTickets()) {
                orderTicketService.createOrderTicket(new Order_TicketDTO(orderId, ticketDTO.getId()));
            }
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
        return orderDetailService.getOrderWithDetailsById(orderId); // Détails inclus dans OrderDetailService
    }

    // Méthode complexe : Créer un ticket pour une commande
    @Transactional
    public TicketDTO createTicketForOrder(Long orderId, TicketDTO ticketDTO) throws Exception {
        logger.info("Création du ticket pour la commande ID: {}", orderId);

        // Création du ticket via TicketService
        TicketDTO savedTicketDTO = ticketService.createTicket(ticketDTO);

        // Associer le ticket à la commande via Order_TicketService
        orderTicketService.createOrderTicket(new Order_TicketDTO(orderId, savedTicketDTO.getId()));

        return savedTicketDTO;
    }

    // Méthode complexe : Mettre à jour un ticket pour une commande
    @Transactional
    public TicketDTO updateTicketForOrder(Long orderId, Long ticketId, TicketDTO ticketDTO) throws Exception {
        logger.info("Mise à jour du ticket ID: {} pour la commande ID: {}", ticketId, orderId);

        // Mise à jour du ticket via TicketService
        TicketDTO updatedTicketDTO = ticketService.updateTicket(ticketId, ticketDTO);

        // Vérifier si le ticket est bien associé à la commande via Order_TicketService
        Order_TicketDTO orderTicketDTO = orderTicketService.getOrderTicketById(ticketId);
        if (orderTicketDTO.getOrderId() != orderId) {
            throw new Exception("Le ticket ID: " + ticketId + " n'est pas associé à la commande ID: " + orderId);
        }

        return updatedTicketDTO;
    }

    // Méthode complexe : Récupérer tous les tickets pour une commande
    @Transactional(readOnly = true)
    public List<TicketDTO> getAllTicketsForOrder(Long orderId) throws Exception {
        logger.info("Récupération de tous les tickets pour la commande ID: {}", orderId);

        // Récupérer les tickets associés à la commande via Order_TicketService
        List<Order_TicketDTO> orderTicketDTOs = orderTicketService.getOrderTicketsByOrder(orderId);

        // Convertir les relations en tickets
        List<TicketDTO> tickets = new ArrayList<>();
        for (Order_TicketDTO orderTicketDTO : orderTicketDTOs) {
            TicketDTO ticketDTO = ticketService.getTicketById(orderTicketDTO.getTicketId());
            tickets.add(ticketDTO);
        }

        return tickets;
    }

    // Méthode complexe : Supprimer un ticket pour une commande
    @Transactional
    public void deleteTicketForOrder(Long orderId, Long ticketId) throws Exception {
        logger.info("Suppression du ticket ID: {} pour la commande ID: {}", ticketId, orderId);

        // Vérifier si le ticket est bien associé à la commande
        Order_TicketDTO orderTicketDTO = orderTicketService.getOrderTicketById(ticketId);
        if (orderTicketDTO.getOrderId() != orderId) {
            throw new Exception("Le ticket ID: " + ticketId + " n'est pas associé à la commande ID: " + orderId);
        }

        // Supprimer le ticket via TicketService
        ticketService.deleteTicket(ticketId);
    }
    
    @Transactional
    public void assignRolesToUser(Long userId, List<String> roleNames) throws Exception {
        logger.info("Assignation de rôles à l'utilisateur ID: {}", userId);
        
        // Récupérer l'utilisateur
        User user = userService.findById(userId);
        
        // Récupérer les rôles par leurs noms
        List<Role> roles = roleService.getRolesByNames(roleNames);
        
        if (roles.isEmpty()) {
            throw new Exception("Les rôles spécifiés n'existent pas");
        }

        // Assigner les rôles à l'utilisateur
        user.setRoles(roles);
        
        // Sauvegarder les modifications de l'utilisateur
        userService.updateUserByEmail(user.getEmail(), userMapper.toDTO(user));
    }
   
    @Transactional
    public void removeTicketFromOrder(Long orderId, Long ticketId) throws Exception {
        logger.info("Suppression du ticket ID: {} de la commande ID: {}", ticketId, orderId);
        
        // Vérifier si le ticket est bien associé à la commande via Order_TicketService
        Order_TicketDTO orderTicketDTO = orderTicketService.getOrderTicketById(ticketId);
        if (orderTicketDTO.getOrderId() != orderId) {
            throw new Exception("Le ticket n'est pas associé à cette commande");
        }

        // Supprimer l'association dans Order_TicketService
        orderTicketService.deleteOrderTicket(orderTicketDTO.getId());

        // Mettre à jour le ticket pour supprimer l'association à la commande
        TicketDTO ticketDTO = ticketService.getTicketById(ticketId);
        ticketDTO.setOrder(null);
        ticketService.updateTicket(ticketId, ticketDTO);  // Sauvegarder la mise à jour
    }

}
