package com.joparis2024.service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.model.Order;
import com.joparis2024.repository.OrderRepository;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper; // Utilisation de OrderMapper

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {
        System.out.println("Tentative de création de la commande avec OrderDTO : " + orderDTO);

        // Utilisation du mapper pour transformer OrderDTO en Order
        Order order = orderMapper.toEntity(orderDTO);

        // Sauvegarde de la commande
        if (entityManager.contains(order)) {
            System.out.println("Merging order entity as it is managed.");
            entityManager.merge(order);
        } else {
            System.out.println("Persisting a new order entity.");
            orderRepository.save(order);
        }

        System.out.println("Commande persistée avec succès : " + order);

        return orderMapper.toDTO(order); // Utilisation du mapper pour transformer Order en DTO
    }

    @Transactional
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) throws Exception {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (!existingOrder.isPresent()) {
            throw new Exception("Order non trouvée");
        }

        Order order = existingOrder.get();
        // Mettre à jour les attributs de la commande via le mapper
        order = orderMapper.toEntity(orderDTO);

        return orderMapper.toDTO(orderRepository.save(order)); // Renvoi du DTO
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() throws Exception {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toDTOs(orders); // Utilisation du mapper pour transformer les entités en DTO
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long orderId) throws Exception {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (!existingOrder.isPresent()) {
            throw new Exception("Order non trouvée");
        }

        return orderMapper.toDTO(existingOrder.get()); // Utilisation du mapper
    }

    @Transactional
    public void cancelOrder(Long orderId) throws Exception {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (!existingOrder.isPresent()) {
            throw new Exception("Order non trouvée");
        }
        orderRepository.delete(existingOrder.get());
    }

    // Méthode pour récupérer une commande par son ID
    @Transactional(readOnly = true)
    public Order findById(Long id) throws Exception {
        return orderRepository.findById(id)
                .orElseThrow(() -> new Exception("Commande non trouvée avec l'id : " + id));
    }
}
