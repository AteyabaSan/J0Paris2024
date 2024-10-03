package com.joparis2024.service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Order;
import com.joparis2024.model.Order_Ticket;
import com.joparis2024.model.Payment;
import com.joparis2024.model.Ticket;
import com.joparis2024.model.User;
import com.joparis2024.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private TicketService ticketService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    @Lazy
    private PaymentService paymentService;

    public Order createOrder(OrderDTO orderDTO) throws Exception {
        System.out.println("Tentative de création de la commande avec OrderDTO : " + orderDTO);

        if (orderDTO == null) {
            throw new Exception("Le DTO de la commande est nul.");
        }

        Order order = new Order();

        if (orderDTO.getUser() == null || orderDTO.getUser().getId() == null) {
            throw new Exception("L'utilisateur est manquant ou incomplet.");
        }

        User user = userService.findById(orderDTO.getUser().getId());
        if (user == null) {
            throw new Exception("L'utilisateur est introuvable");
        }
        order.setUser(user);

        order.setStatus(orderDTO.getStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setOrderDate(orderDTO.getOrderDate());

        if (orderDTO.getPayment() != null) {
            Payment payment = paymentService.mapToEntity(orderDTO.getPayment());
            order.setPayment(payment);
        } else {
            throw new Exception("Le paiement est obligatoire.");
        }

        if (orderDTO.getTickets() == null || orderDTO.getTickets().isEmpty()) {
            throw new Exception("La commande doit contenir au moins un ticket.");
        }

        List<Order_Ticket> orderTickets = new ArrayList<>();
        for (TicketDTO ticketDTO : orderDTO.getTickets()) {
            Ticket ticket = ticketService.mapToEntity(ticketDTO);
            Order_Ticket orderTicket = new Order_Ticket();
            orderTicket.setOrder(order);
            orderTicket.setTicket(ticket);
            orderTicket.setQuantity(ticketDTO.getQuantity());

            orderTickets.add(orderTicket);
        }
        order.setOrderTickets(orderTickets);

        return orderRepository.save(order);
    }

    public Order updateOrder(Long orderId, OrderDTO orderDTO) throws Exception {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (!existingOrder.isPresent()) {
            throw new Exception("Order non trouvée");
        }

        Order order = existingOrder.get();
        order.setStatus(orderDTO.getStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setOrderDate(orderDTO.getOrderDate());

        List<Order_Ticket> orderTickets = new ArrayList<>();
        for (TicketDTO ticketDTO : orderDTO.getTickets()) {
            Ticket ticket = ticketService.mapToEntity(ticketDTO);
            Order_Ticket orderTicket = new Order_Ticket();
            orderTicket.setOrder(order);
            orderTicket.setTicket(ticket);
            orderTicket.setQuantity(ticketDTO.getQuantity());

            orderTickets.add(orderTicket);
        }
        order.setOrderTickets(orderTickets);

        return orderRepository.save(order);
    }

    public List<OrderDTO> getAllOrders() throws Exception {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(mapToDTO(order));
        }
        return orderDTOs;
    }

    public OrderDTO getOrderById(Long orderId) throws Exception {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (!existingOrder.isPresent()) {
            throw new Exception("Order non trouvée");
        }
        return mapToDTO(existingOrder.get());
    }

    public void cancelOrder(Long orderId) throws Exception {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (!existingOrder.isPresent()) {
            throw new Exception("Order non trouvée");
        }
        orderRepository.delete(existingOrder.get());
    }

    public OrderDTO mapToDTO(Order order) throws Exception {
        if (order == null) {
            throw new Exception("L'ordre à mapper est nul.");
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUser(userService.mapToDTO(order.getUser()));
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setOrderDate(order.getOrderDate());

        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Order_Ticket orderTicket : order.getOrderTickets()) {
            ticketDTOs.add(ticketService.mapToDTO(orderTicket.getTicket()));
        }
        orderDTO.setTickets(ticketDTOs);

        return orderDTO;
    }

    public Order mapToEntity(OrderDTO orderDTO) throws Exception {
        if (orderDTO == null) {
            throw new Exception("Le DTO de commande à mapper est nul.");
        }

        Order order = new Order();
        order.setId(orderDTO.getId());

        if (orderDTO.getUser() == null || orderDTO.getUser().getId() == null) {
            throw new Exception("L'utilisateur dans OrderDTO est manquant ou incomplet.");
        }
        order.setUser(userService.mapToEntity(orderDTO.getUser()));

        order.setStatus(orderDTO.getStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setOrderDate(orderDTO.getOrderDate());

        if (orderDTO.getTickets() == null || orderDTO.getTickets().isEmpty()) {
            throw new Exception("La commande doit contenir au moins un ticket.");
        }

        List<Order_Ticket> orderTickets = new ArrayList<>();
        for (TicketDTO ticketDTO : orderDTO.getTickets()) {
            Ticket ticket = ticketService.mapToEntity(ticketDTO);
            Order_Ticket orderTicket = new Order_Ticket();
            orderTicket.setOrder(order);
            orderTicket.setTicket(ticket);
            orderTicket.setQuantity(ticketDTO.getQuantity());

            orderTickets.add(orderTicket);
        }
        order.setOrderTickets(orderTickets);

        if (orderDTO.getPayment() == null || orderDTO.getPayment().getId() == null) {
            throw new Exception("Le paiement est obligatoire.");
        }
        order.setPayment(paymentService.mapToEntity(orderDTO.getPayment()));

        return order;
    }
}