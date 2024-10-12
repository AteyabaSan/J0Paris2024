package com.joparis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.cache.annotation.Cacheable;
import com.joparis2024.model.Ticket;
import com.joparis2024.model.Event;
import com.joparis2024.model.Order;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Récupérer les tickets par événement
    @Cacheable("ticketsByEvent")
    List<Ticket> findByEvent(Event event);

    // Récupérer les tickets par événement et commande
    List<Ticket> findByEventAndOrder(Event event, Order order);

    // Récupérer les tickets liés à une commande
    List<Ticket> findByOrder(Order order);

    // Récupérer les tickets disponibles pour un événement
    List<Ticket> findByEventAndIsAvailableTrue(Event event);

    // Requête avec JOIN FETCH pour récupérer un ticket avec son événement associé
    @Query("SELECT t FROM Ticket t JOIN FETCH t.event WHERE t.id = :id")
    Optional<Ticket> findByIdWithEvent(@Param("id") Long id);

    // Optionnel : Récupérer un ticket avec son événement et commande associés
    @Query("SELECT t FROM Ticket t JOIN FETCH t.event JOIN FETCH t.order WHERE t.id = :id")
    Optional<Ticket> findByIdWithEventAndOrder(@Param("id") Long id);
    
    List<Ticket> findByEventId(Long eventId);
    
    @Query("SELECT t FROM Ticket t JOIN FETCH t.event WHERE t.event.id = :eventId")
    List<Ticket> findTicketsWithEvent(@Param("eventId") Long eventId);
}


