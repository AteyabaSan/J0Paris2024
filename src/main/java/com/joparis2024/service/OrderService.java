package com.joparis2024.service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.OrderSimpleDTO;
import com.joparis2024.dto.TicketDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.model.Order;
import com.joparis2024.model.Order_Ticket;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper; 
    
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {
        logger.info("Tentative de création de la commande avec OrderDTO : {}", orderDTO);

        // Transformation DTO vers entité
        Order order = orderMapper.toEntity(orderDTO);

        // Si l'utilisateur est présent dans le DTO, on récupère l'utilisateur via le service
        if (orderDTO.getUser() != null) {
            order.setUser(userService.findById(orderDTO.getUser().getId()));
        }

        // Gestion des tickets associés à la commande
        if (orderDTO.getTickets() != null && !orderDTO.getTickets().isEmpty()) {
            List<Order_Ticket> orderTickets = new ArrayList<>();
            for (TicketDTO ticketDTO : orderDTO.getTickets()) {
                // Ici on récupère l'entité Ticket directement, pas un TicketDTO
                Ticket ticket = ticketService.getTicketById(ticketDTO.getId()); // Assure-toi que cette méthode retourne un Ticket
                Order_Ticket orderTicket = new Order_Ticket();
                orderTicket.setTicket(ticket); // Lier le ticket à l'ordre
                orderTicket.setOrder(order);
                orderTickets.add(orderTicket);
            }
            order.setOrderTickets(orderTickets); // Assigner les tickets à la commande
        }

        // Sauvegarde de la commande
        Order savedOrder = orderRepository.save(order);
        logger.info("Commande créée avec succès : {}", savedOrder.getId());

        return orderMapper.toDTO(savedOrder); // Retourne le DTO de la commande sauvegardée
    }

    @Transactional
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) throws Exception {
        logger.info("Mise à jour de la commande ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order non trouvée"));

        // Mise à jour de l'utilisateur s'il est présent dans le DTO
        if (orderDTO.getUser() != null) {
            order.setUser(userService.findById(orderDTO.getUser().getId()));
        }

        // Mise à jour des tickets associés
        if (orderDTO.getTickets() != null && !orderDTO.getTickets().isEmpty()) {
            List<Order_Ticket> orderTickets = new ArrayList<>();
            for (TicketDTO ticketDTO : orderDTO.getTickets()) {
            	Ticket ticket = ticketService.getTicketById(ticketDTO.getId());
                Order_Ticket orderTicket = new Order_Ticket();
                orderTicket.setTicket(ticket);
                orderTicket.setOrder(order);
                orderTickets.add(orderTicket);
            }
            order.setOrderTickets(orderTickets); // Mettre à jour les tickets liés à la commande
        }

        Order updatedOrder = orderRepository.save(order);
        logger.info("Commande mise à jour avec succès : {}", updatedOrder.getId());

        return orderMapper.toDTO(updatedOrder);
    }
    
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() throws Exception {
        List<Order> orders = orderRepository.findAll();
        logger.info("Nombre de commandes récupérées: {}", orders.size());
        
        // Transformation des entités Order en DTOs
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(orderMapper.toDTO(order));
        }

        return orderDTOs;
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long orderId) throws Exception {
        logger.info("Recherche de la commande ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));

        // Transformation de l'entité Order en DTO
        return orderMapper.toDTO(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) throws Exception {
        logger.info("Annulation de la commande ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));

        // Suppression de la commande
        orderRepository.delete(order);
        logger.info("Commande annulée avec succès : {}", orderId);
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) throws Exception {
        logger.info("Recherche de la commande avec l'ID: {}", id);

        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée avec l'ID : " + id));
    }
    
    @Transactional(readOnly = true)
    public List<OrderSimpleDTO> getAllSimpleOrders() throws Exception {
        List<Order> orders = orderRepository.findAll();
        logger.info("Nombre de commandes récupérées: {}", orders.size());
        
        // Transformation des entités Order en OrderSimpleDTOs
        List<OrderSimpleDTO> orderSimpleDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderSimpleDTOs.add(orderMapper.toSimpleDTO(order));  // Utilisation du OrderMapper pour mapper vers OrderSimpleDTO
        }

        return orderSimpleDTOs;
    }
    
    @Transactional(readOnly = true)
    public OrderSimpleDTO getSimpleOrderById(Long orderId) throws Exception {
        logger.info("Recherche de la commande ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));

        // Transformation de l'entité Order en OrderSimpleDTO
        return orderMapper.toSimpleDTO(order);
    }


}
