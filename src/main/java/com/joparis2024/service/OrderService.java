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

        orderDTO.setTotalAmount(0.0); // Montant initial
        Order order = orderMapper.toEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDTO(savedOrder);
    }


    @Transactional
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) throws Exception {
        logger.info("Mise à jour de la commande ID: {}", orderId);

        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));

        existingOrder.setOrderDate(orderDTO.getOrderDate());
        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setTotalAmount(orderDTO.getTotalAmount());

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
    
    public Order findOrderEntityById(Long orderId) throws Exception {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new Exception("Commande non trouvée"));
    }
    
    @Transactional
    public Order updateOrder(Long orderId, Order order) throws Exception {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Commande non trouvée."));

        existingOrder.setOrderDate(order.getOrderDate());
        existingOrder.setStatus(order.getStatus());
        existingOrder.setTotalAmount(order.getTotalAmount());

        return orderRepository.save(existingOrder);
    }


}
