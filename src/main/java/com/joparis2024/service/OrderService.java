package com.joparis2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Order;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy //Injection différée pour éviter la boucle
    private TicketService ticketService;

    @Autowired
    private OrderRepository orderRepository;

    // Créer une commande
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        Order order = new Order();
        order.setUser(userService.mapToEntity(orderDTO.getUser()));  // Mapper l'utilisateur
        if (order.getUser() == null) {
            throw new Exception("L'utilisateur est introuvable");
        }
        order.setStatus(orderDTO.getStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setPaymentDate(orderDTO.getPaymentDate());

        List<Ticket> tickets = new ArrayList<>();
        for (TicketDTO ticketDTO : orderDTO.getTickets()) {
            Ticket ticket = ticketService.mapToEntity(ticketDTO);  // Mapper les tickets
            tickets.add(ticket);
        }
        order.setTickets(tickets);

        return orderRepository.save(order);
    }

    // Mettre à jour une commande
    public Order updateOrder(Long orderId, OrderDTO orderDTO) throws Exception {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (!existingOrder.isPresent()) {
            throw new Exception("Order non trouvée");
        }

        Order order = existingOrder.get();
        order.setStatus(orderDTO.getStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setPaymentDate(orderDTO.getPaymentDate());

        List<Ticket> tickets = new ArrayList<>();
        for (TicketDTO ticketDTO : orderDTO.getTickets()) {
            Ticket ticket = ticketService.mapToEntity(ticketDTO);  // Mapper les tickets
            tickets.add(ticket);
        }
        order.setTickets(tickets);

        return orderRepository.save(order);
    }

    // Récupérer toutes les commandes
    public List<OrderDTO> getAllOrders() throws Exception {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(mapToDTO(order));
        }
        return orderDTOs;
    }

    // Annuler une commande
    public void cancelOrder(Long orderId) throws Exception {
        try {
            Optional<Order> existingOrder = orderRepository.findById(orderId);
            if (!existingOrder.isPresent()) {
                throw new Exception("Order non trouvée");
            }
            orderRepository.delete(existingOrder.get());
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'annulation de la commande: " + e.getMessage());
        }
    }

    // Mapper Order -> OrderDTO (pour les méthodes getAllOrders et autres)
    public OrderDTO mapToDTO(Order order) throws Exception {
        try {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());  // Ajout de l'ID ici
            orderDTO.setUser(userService.mapToDTO(order.getUser()));  // Mapper l'utilisateur
            orderDTO.setStatus(order.getStatus());
            orderDTO.setTotalAmount(order.getTotalAmount());
            orderDTO.setPaymentDate(order.getPaymentDate());

            List<TicketDTO> ticketDTOs = new ArrayList<>();
            for (Ticket ticket : order.getTickets()) {
                ticketDTOs.add(ticketService.mapToDTO(ticket));  // Mapper les tickets
            }
            orderDTO.setTickets(ticketDTOs);

            return orderDTO;
        } catch (Exception e) {
            throw new Exception("Erreur lors du mappage Order -> OrderDTO: " + e.getMessage());
        }
    }

    // Mapper OrderDTO -> Order (Entity)
    public Order mapToEntity(OrderDTO orderDTO) throws Exception {
        try {
            Order order = new Order();
            order.setId(orderDTO.getId());  // Ajout de l'ID ici
            order.setUser(userService.mapToEntity(orderDTO.getUser()));  // Mapper l'utilisateur
            order.setStatus(orderDTO.getStatus());
            order.setTotalAmount(orderDTO.getTotalAmount());
            order.setPaymentDate(orderDTO.getPaymentDate());

            List<Ticket> tickets = new ArrayList<>();
            for (TicketDTO ticketDTO : orderDTO.getTickets()) {
                Ticket ticket = ticketService.mapToEntity(ticketDTO);  // Mapper les tickets
                tickets.add(ticket);
            }
            order.setTickets(tickets);

            return order;
        } catch (Exception e) {
            throw new Exception("Erreur lors du mappage OrderDTO -> Order: " + e.getMessage());
        }
    }
}

