package com.joparis2024.service;

import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.TicketMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderTicketFacade {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketMapper ticketMapper;
    
    @Autowired
    private Order_TicketService orderTicketService;

    public void assignTicketToOrder(Long ticketId, Long orderId) throws Exception {
        // Récupérer et convertir le Ticket en DTO
        TicketDTO ticketDTO = ticketMapper.toDTO(ticketService.getTicketById(ticketId));  // Utilise TicketDTO

        // Créer un DTO Order_Ticket pour lier l'ordre et le ticket (orderId est passé directement)
        Order_TicketDTO orderTicketDTO = new Order_TicketDTO(null, orderId, ticketDTO.getId(), 1);

        // Sauvegarde de la relation via Order_TicketService
        orderTicketService.createOrderTicket(orderTicketDTO);
    }

    // Exemple de méthode supplémentaire pour dissocier les tickets
    public void removeTicketFromOrder(Long ticketId, Long orderId) throws Exception {
        // Logique pour dissocier un ticket d'une commande
        // Cela dépend de ton implémentation dans Order_TicketService
    }
}
