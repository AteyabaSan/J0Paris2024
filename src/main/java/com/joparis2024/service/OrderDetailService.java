package com.joparis2024.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.model.Order;
import com.joparis2024.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderDetailService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public OrderDTO getOrderWithDetailsById(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));

        // Transformation en OrderDTO complet (avec tous les détails comme les tickets, transactions, paiements)
        return orderMapper.toDTO(order); // toDTO gère déjà les relations complexes
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrdersWithDetails() throws Exception {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(orderMapper.toDTO(order));  // Mapping complet avec tous les détails
        }
        return orderDTOs;
    }
}

