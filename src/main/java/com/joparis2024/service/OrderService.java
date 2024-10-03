package com.joparis2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Order;
import com.joparis2024.model.Order_Ticket;
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
    @Lazy // Injection différée pour éviter la boucle
    private TicketService ticketService;

    @Autowired
    private OrderRepository orderRepository;
 // Créer une commande (CREATE)
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        Order order = new Order();
        order.setUser(userService.mapToEntity(orderDTO.getUser()));  // Mapper l'utilisateur
        
        if (order.getUser() == null) {
            throw new Exception("L'utilisateur est introuvable");
        }

        order.setStatus(orderDTO.getStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setOrderDate(orderDTO.getOrderDate());  // Date de la commande

        // Vérifier que la commande contient au moins un ticket
        if (orderDTO.getTickets() == null || orderDTO.getTickets().isEmpty()) {
            throw new Exception("La commande doit contenir au moins un ticket.");
        }

        // Gérer les tickets
        List<Order_Ticket> orderTickets = new ArrayList<>();
        for (TicketDTO ticketDTO : orderDTO.getTickets()) {
            Ticket ticket = ticketService.mapToEntity(ticketDTO);  // Mapper les tickets

            Order_Ticket orderTicket = new Order_Ticket();
            orderTicket.setOrder(order);
            orderTicket.setTicket(ticket);
            orderTicket.setQuantity(ticketDTO.getQuantity());  // Gérer la quantité via Order_Ticket

            orderTickets.add(orderTicket);
        }
        
        order.setOrderTickets(orderTickets);  // Associer les tickets à la commande

        return orderRepository.save(order);  // Sauvegarder la commande et retourner le résultat
    }

    // Mettre à jour une commande (UPDATE)
    public Order updateOrder(Long orderId, OrderDTO orderDTO) throws Exception {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (!existingOrder.isPresent()) {
            throw new Exception("Order non trouvée");
        }

        Order order = existingOrder.get();
        order.setStatus(orderDTO.getStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setOrderDate(orderDTO.getOrderDate());  // Date de la commande

        // Gestion des tickets via Order_Ticket
        List<Order_Ticket> orderTickets = new ArrayList<>();
        for (TicketDTO ticketDTO : orderDTO.getTickets()) {
            Ticket ticket = ticketService.mapToEntity(ticketDTO);  // Mapper les tickets
            
            Order_Ticket orderTicket = new Order_Ticket();
            orderTicket.setOrder(order);
            orderTicket.setTicket(ticket);
            orderTicket.setQuantity(ticketDTO.getQuantity());  // Gérer la quantité via Order_Ticket
            
            orderTickets.add(orderTicket);
        }
        order.setOrderTickets(orderTickets);

        return orderRepository.save(order);
    }

    // Récupérer toutes les commandes (READ)
    public List<OrderDTO> getAllOrders() throws Exception {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(mapToDTO(order));
        }
        return orderDTOs;
    }

    // Récupérer une commande par ID (READ)
    public OrderDTO getOrderById(Long orderId) throws Exception {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (!existingOrder.isPresent()) {
            throw new Exception("Order non trouvée");
        }
        return mapToDTO(existingOrder.get());
    }

    // Annuler une commande (DELETE)
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
            orderDTO.setOrderDate(order.getOrderDate());  // Date de la commande

            List<TicketDTO> ticketDTOs = new ArrayList<>();
            for (Order_Ticket orderTicket : order.getOrderTickets()) {
                ticketDTOs.add(ticketService.mapToDTO(orderTicket.getTicket()));  // Mapper les tickets
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
            order.setOrderDate(orderDTO.getOrderDate());  // Date de la commande

            List<Order_Ticket> orderTickets = new ArrayList<>();
            for (TicketDTO ticketDTO : orderDTO.getTickets()) {
                Ticket ticket = ticketService.mapToEntity(ticketDTO);  // Mapper les tickets
                
                Order_Ticket orderTicket = new Order_Ticket();
                orderTicket.setOrder(order);
                orderTicket.setTicket(ticket);
                orderTicket.setQuantity(ticketDTO.getQuantity());  // Gérer la quantité via Order_Ticket
                
                orderTickets.add(orderTicket);
            }
            order.setOrderTickets(orderTickets);

            return order;
        } catch (Exception e) {
            throw new Exception("Erreur lors du mappage OrderDTO -> Order: " + e.getMessage());
        }
    }
}



