package com.joparis2024.service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Order;
import com.joparis2024.model.Order_Ticket;
import com.joparis2024.model.Payment;
import com.joparis2024.model.Ticket;
import com.joparis2024.model.User;
import com.joparis2024.repository.OrderRepository;
import com.joparis2024.repository.TicketRepository;
import com.joparis2024.repository.UserRepository;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    @Lazy
    private TicketService ticketService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EntityManager entityManager; // Pour utiliser merge sur les entités détachées

    @Transactional
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        System.out.println("Tentative de création de la commande avec OrderDTO : " + orderDTO);

        // Mapping de OrderDTO à l'entité Order
        Order order = new Order();
        order.setStatus(orderDTO.getStatus());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setTotalAmount(orderDTO.getTotalAmount());
        System.out.println("Order mappée: " + order);

        // Récupération de l'utilisateur
        Optional<User> optionalUser = userRepository.findById(orderDTO.getUser().getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            order.setUser(user);
            System.out.println("Utilisateur trouvé: " + user);
        } else {
            throw new Exception("Utilisateur non trouvé.");
        }

        // Mapping des Order_Ticket (tickets liés à la commande)
        List<Order_Ticket> orderTickets = new ArrayList<>();
        for (TicketDTO ticketDTO : orderDTO.getTickets()) {
            System.out.println("Traitement du TicketDTO : " + ticketDTO);
            
            // Création d'un Order_Ticket pour chaque TicketDTO
            Order_Ticket orderTicket = new Order_Ticket();
            Optional<Ticket> optionalTicket = ticketRepository.findById(ticketDTO.getId());
            if (optionalTicket.isPresent()) {
                Ticket ticket = optionalTicket.get();
                orderTicket.setTicket(ticket);
                orderTicket.setQuantity(ticketDTO.getQuantity());
                orderTicket.setOrder(order); // Lier le ticket à la commande
                orderTickets.add(orderTicket);
                System.out.println("Order_Ticket ajouté : " + orderTicket);
            } else {
                throw new Exception("Ticket non trouvé.");
            }
        }
        order.setOrderTickets(orderTickets);
        System.out.println("Liste des Order_Tickets associés à la commande : " + orderTickets);

        // Mapping de PaymentDTO à l'entité Payment
        PaymentDTO paymentDTO = orderDTO.getPayment();
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());
        System.out.println("Payment mappé : " + payment);

        // Associer le paiement à la commande
        payment.setOrder(order);
        order.setPayment(payment);
        System.out.println("Payment après association à la commande: " + payment);
        System.out.println("Order après association du paiement: " + order);

        // Sauvegarder la commande et gérer les entités détachées
        if (entityManager.contains(order)) {
            System.out.println("Merging order entity as it is managed.");
            entityManager.merge(order);
        } else {
            System.out.println("Persisting a new order entity.");
            orderRepository.save(order);
        }

        System.out.println("Commande persistée avec succès : " + order);

        return order;
    }



    @Transactional
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

    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() throws Exception {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(mapToDTO(order));
        }
        return orderDTOs;
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long orderId) throws Exception {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (!existingOrder.isPresent()) {
            throw new Exception("Order non trouvée");
        }
        return mapToDTO(existingOrder.get());
    }

    @Transactional
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