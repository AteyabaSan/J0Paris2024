package com.joparis2024.service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.mapper.OrderMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.joparis2024.model.Order;
import com.joparis2024.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {
        logger.info("Création d'une nouvelle commande : {}", orderDTO);

        // Conversion du DTO en entité Order
        Order order = orderMapper.toEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);

        // Conversion de l'entité sauvegardée en DTO pour le retour
        return orderMapper.toDTO(savedOrder);
    }

    @Transactional
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) throws Exception {
        logger.info("Mise à jour de la commande ID: {}", orderId);

        // Recherche de la commande existante
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));

        // Mise à jour des détails de la commande
        existingOrder.setOrderDate(orderDTO.getOrderDate());
        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setTotalAmount(orderDTO.getTotalAmount());

        // Sauvegarde des modifications
        Order updatedOrder = orderRepository.save(existingOrder);

        return orderMapper.toDTO(updatedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() throws Exception {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toDTOs(orders);
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long orderId) throws Exception {
        logger.info("Récupération de la commande avec ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));
        return orderMapper.toDTO(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) throws Exception {
        logger.info("Annulation de la commande avec ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));
        order.setStatus("ANNULÉ");
        orderRepository.save(order);  // Mise à jour du statut
    }
}
