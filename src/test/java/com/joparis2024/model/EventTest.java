package com.joparis2024.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
@ActiveProfiles("test")
class EventTest {

    @Test
    void testEventGettersAndSetters() {
        Event event = new Event();
        event.setId(1L);
        event.setName("Olympics");
        event.setLocation("Paris");
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setCategory("Sports");
        event.setDescription("Olympic Games 2024");

        assertEquals(1L, event.getId());
        assertEquals("Olympics", event.getName());
        assertEquals("Paris", event.getLocation());
        assertEquals(startTime, event.getStartTime());
        assertEquals(endTime, event.getEndTime());
        assertEquals("Sports", event.getCategory());
        assertEquals("Olympic Games 2024", event.getDescription());
    }
}
