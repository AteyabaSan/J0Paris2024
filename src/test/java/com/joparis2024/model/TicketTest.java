package com.joparis2024.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
@ActiveProfiles("test")
class TicketTest {

    @Test
    void testTicketGettersAndSetters() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        Event event = new Event();
        User owner = new User();
        Order order = new Order();
        
        ticket.setEvent(event);
        ticket.setOwner(owner);
        ticket.setOrder(order);
        ticket.setPrice(50.0);
        ticket.setSeatNumber("A1");
        ticket.setIsAvailable(true);
        ticket.setQrCode("QR12345");

        assertEquals(1L, ticket.getId());
        assertEquals(event, ticket.getEvent());
        assertEquals(owner, ticket.getOwner());
        assertEquals(order, ticket.getOrder());
        assertEquals(50.0, ticket.getPrice());
        assertEquals("A1", ticket.getSeatNumber());
        assertTrue(ticket.getIsAvailable());
        assertEquals("QR12345", ticket.getQrCode());
    }
}
