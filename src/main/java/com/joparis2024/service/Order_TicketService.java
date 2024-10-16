package com.joparis2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.mapper.Order_TicketMapper;
import com.joparis2024.model.Order;
import com.joparis2024.model.Order_Ticket;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.OrderRepository;
import com.joparis2024.repository.Order_TicketRepository;
import com.joparis2024.repository.TicketRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class Order_TicketService {

    @Autowired
    private Order_TicketRepository orderTicketRepository;

    @Autowired
    private Order_TicketMapper orderTicketMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TicketRepository ticketRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

 // Créer une association Order_Ticket
    public Order_TicketDTO createOrderTicket(Order_TicketDTO orderTicketDTO) throws Exception {
        if (orderTicketDTO.getOrderId() == null || orderTicketDTO.getTicketId() == null) {
            throw new IllegalArgumentException("Order ID et Ticket ID ne doivent pas être nuls.");
        }

        Order order = orderRepository.findById(orderTicketDTO.getOrderId())
                .orElseThrow(() -> new Exception("Commande non trouvée."));
        Ticket ticket = ticketRepository.findById(orderTicketDTO.getTicketId())
                .orElseThrow(() -> new Exception("Ticket non trouvé."));

        // Validation de la quantité
        if (orderTicketDTO.getQuantity() == null || orderTicketDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure à 0.");
        }

        logger.info("Association du ticket ID {} à la commande ID {} avec quantité: {}", ticket.getId(), order.getId(), orderTicketDTO.getQuantity());

        Order_Ticket orderTicket = orderTicketMapper.toEntity(orderTicketDTO);
        orderTicket.setOrder(order);
        orderTicket.setTicket(ticket);
        orderTicket.setQuantity(orderTicketDTO.getQuantity());

        Order_Ticket savedOrderTicket = orderTicketRepository.save(orderTicket);
        return orderTicketMapper.toDTO(savedOrderTicket);
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
        return orderTicketMapper.toDTO(orderTicket);
    }
    
 // Service pour récupérer toutes les associations Order_Ticket
    public List<Order_TicketDTO> getAllOrderTickets() {
        List<Order_Ticket> orderTickets = orderTicketRepository.findAll();
        return orderTicketMapper.toDTOs(orderTickets);
    }
    
 // Ajoute cette méthode à Order_TicketService
    public List<Order_TicketDTO> getOrderTicketsByOrder(Long orderId) throws Exception {
        // Récupère toutes les entrées Order_Ticket par ID de commande
        List<Order_Ticket> orderTickets = orderTicketRepository.findByOrderId(orderId);
        
        // Convertir la liste des entités en DTOs
        return orderTicketMapper.toDTOs(orderTickets);
    }



}
