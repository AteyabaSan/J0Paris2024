package com.joparis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.Ticket;
import com.joparis2024.model.Event;
import com.joparis2024.model.Order;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEvent(Event event);
    List<Ticket> findByEventAndOrder(Event event, Order order);

}
