package com.joparis2024.repository;

import com.joparis2024.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integration")  // Indique que ce test utilise le profil "integration"
@Transactional  // Utilise les transactions pour que les données soient effacées après chaque test
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void testSaveAndFindById() {
        // Given
        Event event = new Event();
        event.setName("Jeux Olympiques");
        event.setLocation("Paris");
        event.setCategory("Sports");
        event.setStartTime(LocalDateTime.now());
        event.setEndTime(LocalDateTime.now().plusHours(2));
        eventRepository.save(event);

        // When
        Event foundEvent = eventRepository.findById(event.getId()).orElse(null);

        // Then
        assertThat(foundEvent).isNotNull();
        assertThat(foundEvent.getName()).isEqualTo("Jeux Olympiques");
        assertThat(foundEvent.getLocation()).isEqualTo("Paris");
    }
}
