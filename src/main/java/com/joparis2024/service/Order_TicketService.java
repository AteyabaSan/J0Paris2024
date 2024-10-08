package com.joparis2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.mapper.Order_TicketMapper;
import com.joparis2024.model.Order_Ticket;
import com.joparis2024.repository.Order_TicketRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class Order_TicketService {

    @Autowired
    private Order_TicketRepository orderTicketRepository;

    @Autowired
    private Order_TicketMapper orderTicketMapper;

    // Créer une association Order_Ticket
    public Order_TicketDTO createOrderTicket(Order_TicketDTO orderTicketDTO) {
        if (orderTicketDTO.getOrderId() == null || orderTicketDTO.getTicketId() == null) {
            throw new IllegalArgumentException("Order ID and Ticket ID must not be null");
        }

        Order_Ticket orderTicket = orderTicketMapper.toEntity(orderTicketDTO);
        Order_Ticket savedOrderTicket = orderTicketRepository.save(orderTicket);
        return orderTicketMapper.toDTO(savedOrderTicket);
    }

    // Récupérer toutes les associations Order_Ticket pour une commande
    public List<Order_TicketDTO> getOrderTicketsByOrder(Long orderId) {
        List<Order_Ticket> orderTickets = orderTicketRepository.findByOrderId(orderId);
        return orderTicketMapper.toDTOs(orderTickets);
    }

    // Supprimer une association Order_Ticket
    public void deleteOrderTicket(Long id) throws Exception {
        Order_Ticket orderTicket = orderTicketRepository.findById(id)
                .orElseThrow(() -> new Exception("Relation Order-Ticket non trouvée"));
        orderTicketRepository.delete(orderTicket);
    }
    
    public Order_TicketDTO getOrderTicketById(Long ticketId) {
        Order_Ticket orderTicket = orderTicketRepository.findById(ticketId)
            .orElseThrow(() -> new EntityNotFoundException("Order_Ticket non trouvé"));
        return orderTicketMapper.toDTO(orderTicket);  // Utilisation de toDTO() pour une seule entité
    }

}
