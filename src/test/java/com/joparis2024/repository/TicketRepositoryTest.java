package com.joparis2024.repository;

import com.joparis2024.model.Event;
import com.joparis2024.model.Ticket;
import com.joparis2024.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integration")  // Indique que ce test utilise le profil "integration"
@Transactional  // Utilise les transactions pour que les données soient effacées après chaque test
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEvent() {
        // Given
        Event event = new Event();
        event.setName("Jeux Olympiques");
        event.setLocation("Paris");
        event.setCategory("Sports");
        event.setStartTime(LocalDateTime.now());
        event.setEndTime(LocalDateTime.now().plusHours(2));
        eventRepository.save(event);

        Ticket ticket1 = new Ticket();
        ticket1.setEvent(event);
        ticket1.setPrice(50.0);
        ticket1.setSeatNumber("A1");
        ticket1.setIsAvailable(true);
        ticketRepository.save(ticket1);

        Ticket ticket2 = new Ticket();
        ticket2.setEvent(event);
        ticket2.setPrice(75.0);
        ticket2.setSeatNumber("A2");
        ticket2.setIsAvailable(true);
        ticketRepository.save(ticket2);

        // When
        List<Ticket> tickets = ticketRepository.findByEvent(event);

        // Then
        assertThat(tickets).hasSize(2);
        assertThat(tickets).extracting(Ticket::getPrice).containsExactlyInAnyOrder(50.0, 75.0);
    }

    @Test
    void testFindByOwner() {
        // Given
        User owner = new User();
        owner.setUsername("proprietaireBillet");
        owner.setPassword("motDePasseProprietaire");
        owner.setEmail("proprietaire@example.com");
        owner.setRole("UTILISATEUR");
        owner.setEnabled(true);
        userRepository.save(owner);

        Event event = new Event();
        event.setName("Jeux Olympiques");
        event.setLocation("Paris");
        event.setCategory("Sports");
        event.setStartTime(LocalDateTime.now());
        event.setEndTime(LocalDateTime.now().plusHours(2));
        eventRepository.save(event);

        Ticket ticket = new Ticket();
        ticket.setOwner(owner);
        ticket.setEvent(event);
        ticket.setPrice(50.0);
        ticket.setSeatNumber("B1");
        ticket.setIsAvailable(true);
        ticketRepository.save(ticket);

        // When
        List<Ticket> tickets = ticketRepository.findByOwner(owner);

        // Then
        assertThat(tickets).hasSize(1);
        assertThat(tickets.get(0).getOwner()).isEqualTo(owner);
    }
}
