package com.joparis2024.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class UserTest {

    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("securePassword");
        user.setEmail("test@example.com");
        user.setRole("USER");
        user.setEnabled(true);
        user.setPhoneNumber("123456789");

        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("securePassword", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("USER", user.getRole());
        assertTrue(user.getEnabled());
        assertEquals("123456789", user.getPhoneNumber());
    }

    @Test
    void testUserOrdersAssociation() {
        User user = new User();
        Order order1 = new Order();
        Order order2 = new Order();
        
        user.setOrders(List.of(order1, order2));

        assertNotNull(user.getOrders());
        assertEquals(2, user.getOrders().size());
        assertTrue(user.getOrders().contains(order1));
        assertTrue(user.getOrders().contains(order2));
    }
}
