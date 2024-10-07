package com.joparis2024.service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.OrderSimpleDTO;
import com.joparis2024.dto.TicketDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.model.Order;
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
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderTicketFacade orderTicketFacade;  // Utilisation de la façade

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {
        logger.info("Tentative de création de la commande avec OrderDTO : {}", orderDTO);

        // Transformation DTO vers entité
        Order order = orderMapper.toEntity(orderDTO);

        // Si l'utilisateur est présent dans le DTO, on récupère l'utilisateur via le service
        if (orderDTO.getUser() != null) {
            order.setUser(userService.findById(orderDTO.getUser().getId()));
        }

        // Sauvegarde de la commande avant d'ajouter les tickets pour avoir un ordre valide
        Order savedOrder = orderRepository.save(order);
        logger.info("Commande créée avec succès : {}", savedOrder.getId());

        // Utilisation de la façade pour gérer les tickets associés à la commande
        if (orderDTO.getTickets() != null && !orderDTO.getTickets().isEmpty()) {
            for (TicketDTO ticketDTO : orderDTO.getTickets()) {
                orderTicketFacade.assignTicketToOrder(ticketDTO.getId(), savedOrder.getId());
            }
        }

        return orderMapper.toDTO(savedOrder);
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

        // Sauvegarde de la commande avant la mise à jour des tickets pour avoir un ordre valide
        Order updatedOrder = orderRepository.save(order);
        logger.info("Commande mise à jour avec succès : {}", updatedOrder.getId());

        // Mise à jour des tickets associés via la façade
        if (orderDTO.getTickets() != null && !orderDTO.getTickets().isEmpty()) {
            for (TicketDTO ticketDTO : orderDTO.getTickets()) {
                orderTicketFacade.assignTicketToOrder(ticketDTO.getId(), updatedOrder.getId());
            }
        }

        return orderMapper.toDTO(updatedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() throws Exception {
        List<Order> orders = orderRepository.findAll();
        logger.info("Nombre de commandes récupérées: {}", orders.size());

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

        return orderMapper.toDTO(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) throws Exception {
        logger.info("Annulation de la commande ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));

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

        List<OrderSimpleDTO> orderSimpleDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderSimpleDTOs.add(orderMapper.toSimpleDTO(order));
        }

        return orderSimpleDTOs;
    }

    @Transactional(readOnly = true)
    public OrderSimpleDTO getSimpleOrderById(Long orderId) throws Exception {
        logger.info("Recherche de la commande ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));

        return orderMapper.toSimpleDTO(order);
    }
}
