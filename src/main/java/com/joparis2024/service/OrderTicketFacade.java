package com.joparis2024.service;

import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.TicketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderTicketFacade {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private TicketMapper ticketMapper;
    
    @Autowired
    private Order_TicketService orderTicketService;

    
    @Autowired
    public void assignTicketToOrder(Long ticketId, Long orderId) throws Exception {
        // Récupérer et convertir le Ticket en DTO
        TicketDTO ticketDTO = ticketMapper.toDTO(ticketService.getTicketById(ticketId));  // Utilise TicketDTO

        // Récupérer la commande en DTO
        OrderDTO orderDTO = orderService.getOrderById(orderId);

        // Créer un DTO Order_Ticket pour lier l'ordre et le ticket
        Order_TicketDTO orderTicketDTO = new Order_TicketDTO(null, orderDTO.getId(), ticketDTO.getId(), 1);  // Par exemple, tu peux mettre 1 par défaut pour 'quantity'


        // Sauvegarde de la relation via Order_TicketService
        orderTicketService.createOrderTicket(orderTicketDTO);
    }


    // Exemple de méthode supplémentaire pour gérer les relations si nécessaire
    public void removeTicketFromOrder(Long ticketId, Long orderId) throws Exception {
        // Logique pour dissocier un ticket d'une commande
        // Cela peut impliquer l'utilisation de Order_TicketService pour supprimer la relation
        // Cela dépend de ton implémentation dans Order_TicketService
    }
}

