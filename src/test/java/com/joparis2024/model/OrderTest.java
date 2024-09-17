package com.joparis2024.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
@ActiveProfiles("test")
class OrderTest {

    @Test
    void testOrderGettersAndSetters() {
        Order order = new Order();
        order.setId(1L);
        User user = new User();
        order.setUser(user);
        LocalDateTime orderDate = LocalDateTime.now();
        order.setOrderDate(orderDate);
        order.setTotalAmount(100.0);

        assertEquals(1L, order.getId());
        assertEquals(user, order.getUser());
        assertEquals(orderDate, order.getOrderDate());
        assertEquals(100.0, order.getTotalAmount());
    }

    @Test
    void testOrderTicketsAssociation() {
        Order order = new Order();
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        
        order.setTickets(List.of(ticket1, ticket2));

        assertNotNull(order.getTickets());
        assertEquals(2, order.getTickets().size());
        assertTrue(order.getTickets().contains(ticket1));
        assertTrue(order.getTickets().contains(ticket2));
    }
}
