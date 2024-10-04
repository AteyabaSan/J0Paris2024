package com.joparis2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.mapper.Order_TicketMapper;
import com.joparis2024.model.Order_Ticket;
import com.joparis2024.repository.Order_TicketRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class Order_TicketService {

    @Autowired
    private Order_TicketRepository orderTicketRepository;

    @Autowired
    private Order_TicketMapper orderTicketMapper;

    // Créer une association Order_Ticket
    public Order_TicketDTO createOrderTicket(Order_TicketDTO orderTicketDTO) {
        // Vérifier que les ID d'ordre et de ticket sont valides avant de continuer
        if (orderTicketDTO.getOrderId() == null || orderTicketDTO.getTicketId() == null) {
            throw new IllegalArgumentException("Order ID and Ticket ID must not be null");
        }

        Order_Ticket orderTicket = orderTicketMapper.toEntity(orderTicketDTO);
        Order_Ticket savedOrderTicket = orderTicketRepository.save(orderTicket);
        return orderTicketMapper.toDTO(savedOrderTicket);
    }

    // Récupérer tous les Order_Ticket d'une commande
    public List<Order_TicketDTO> getOrderTicketsByOrder(Long orderId) {
        List<Order_Ticket> orderTickets = orderTicketRepository.findByOrderId(orderId);
        List<Order_TicketDTO> orderTicketDTOs = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les entités en DTO
        for (Order_Ticket orderTicket : orderTickets) {
            orderTicketDTOs.add(orderTicketMapper.toDTO(orderTicket));
        }
        return orderTicketDTOs;
    }

    // Récupérer tous les Order_Ticket pour un ticket
    public List<Order_TicketDTO> getOrderTicketsByTicket(Long ticketId) {
        List<Order_Ticket> orderTickets = orderTicketRepository.findByTicketId(ticketId);
        List<Order_TicketDTO> orderTicketDTOs = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les entités en DTO
        for (Order_Ticket orderTicket : orderTickets) {
            orderTicketDTOs.add(orderTicketMapper.toDTO(orderTicket));
        }
        return orderTicketDTOs;
    }
}