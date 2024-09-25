package com.joparis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.Order_Ticket;

import java.util.List;

public interface Order_TicketRepository extends JpaRepository<Order_Ticket, Long> {
    List<Order_Ticket> findByOrderId(Long orderId); // Pour récupérer les tickets d'une commande
    List<Order_Ticket> findByTicketId(Long ticketId); // Pour récupérer les commandes d'un ticket
}
