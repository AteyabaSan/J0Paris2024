package com.joparis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.Ticket;
import com.joparis2024.model.Event;
import com.joparis2024.model.Order;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Récupérer les tickets par événement
    List<Ticket> findByEvent(Event event);

    // Récupérer les tickets par événement et commande
    List<Ticket> findByEventAndOrder(Event event, Order order);

    // Récupérer les tickets liés à une commande
    List<Ticket> findByOrder(Order order);

    // Récupérer les tickets disponibles pour un événement
    List<Ticket> findByEventAndIsAvailableTrue(Event event);
}

