package com.joparis2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.model.Order;
import com.joparis2024.model.Order_Ticket;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.Order_TicketRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Order_TicketService {

    @Autowired
    private Order_TicketRepository orderTicketRepository;

    // Créer une association Order_Ticket
    public Order_TicketDTO createOrderTicket(Order_TicketDTO orderTicketDTO) {
        Order_Ticket orderTicket = new Order_Ticket();
        orderTicket.setOrder(new Order(orderTicketDTO.getOrderId())); // Associer avec l'ID de commande
        orderTicket.setTicket(new Ticket(orderTicketDTO.getTicketId())); // Associer avec l'ID de ticket
        orderTicket.setQuantity(orderTicketDTO.getQuantity());

        Order_Ticket savedOrderTicket = orderTicketRepository.save(orderTicket);
        return mapToDTO(savedOrderTicket);
    }

    // Récupérer tous les Order_Ticket d'une commande
    public List<Order_TicketDTO> getOrderTicketsByOrder(Long orderId) {
        List<Order_Ticket> orderTickets = orderTicketRepository.findByOrderId(orderId);
        return orderTickets.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Récupérer tous les Order_Ticket pour un ticket
    public List<Order_TicketDTO> getOrderTicketsByTicket(Long ticketId) {
        List<Order_Ticket> orderTickets = orderTicketRepository.findByTicketId(ticketId);
        return orderTickets.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Mapper Order_Ticket -> OrderTicketDTO
    private Order_TicketDTO mapToDTO(Order_Ticket orderTicket) {
        return new Order_TicketDTO(
            orderTicket.getId(),
            orderTicket.getOrder().getId(),
            orderTicket.getTicket().getId(),
            orderTicket.getQuantity()
        );
    }
}
