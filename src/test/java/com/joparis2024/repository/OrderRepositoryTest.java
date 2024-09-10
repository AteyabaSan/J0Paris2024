package com.joparis2024.repository;

import com.joparis2024.model.Order;
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
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUser() {
        // Given
        User user = new User();
        user.setUsername("utilisateurCommande");
        user.setPassword("motDePasseCommande");
        user.setEmail("commande@example.com");
        user.setRole("UTILISATEUR");
        user.setEnabled(true);
        userRepository.save(user);

        Order order1 = new Order();
        order1.setUser(user);
        order1.setOrderDate(LocalDateTime.now());
        order1.setTotalAmount(100.0);
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setUser(user);
        order2.setOrderDate(LocalDateTime.now());
        order2.setTotalAmount(200.0);
        orderRepository.save(order2);

        // When
        List<Order> orders = orderRepository.findByUser(user);

        // Then
        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getTotalAmount).containsExactlyInAnyOrder(100.0, 200.0);
    }
}
