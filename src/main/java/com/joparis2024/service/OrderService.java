package com.joparis2024.service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.model.Order;
import com.joparis2024.model.Ticket;
import com.joparis2024.model.User;
import com.joparis2024.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Créer une commande
    public Order createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setUser(new User(orderDTO.getUserId()));  // Récupérer l'utilisateur associé
        order.setOrderDate(orderDTO.getOrderDate());
        order.setTotalAmount(orderDTO.getTotalAmount());
        List<Ticket> tickets = orderDTO.getTicketIds().stream().map(Ticket::new).collect(Collectors.toList());
        order.setTickets(tickets);
        order.setStatus(orderDTO.getStatus());
        return orderRepository.save(order);
    }

    // Récupérer une commande par ID
    public Optional<OrderDTO> getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(this::mapToDTO);
    }

    // Mapper l'entité Order vers un DTO OrderDTO
    private OrderDTO mapToDTO(Order order) {
        List<Long> ticketIds = order.getTickets().stream().map(Ticket::getId).collect(Collectors.toList());
        return new OrderDTO(order.getId(), order.getUser().getId(), order.getOrderDate(), order.getTotalAmount(), ticketIds, order.getStatus());
    }
}
