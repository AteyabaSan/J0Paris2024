package com.joparis2024.repository;

import com.joparis2024.model.Order;
import com.joparis2024.model.Payment;
import com.joparis2024.model.User;
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
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByOrder() {
        // Given
        User user = new User();
        user.setUsername("utilisateurPaiement");
        user.setPassword("motDePassePaiement");
        user.setEmail("paiement@example.com");
        user.setRole("UTILISATEUR");
        user.setEnabled(true);
        userRepository.save(user);

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(150.0);
        orderRepository.save(order);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentStatus("PAYE");
        payment.setPaymentMethod("Carte de Crédit");
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        // When
        Payment foundPayment = paymentRepository.findByOrder(order);

        // Then
        assertThat(foundPayment).isNotNull();
        assertThat(foundPayment.getOrder()).isEqualTo(order);
        assertThat(foundPayment.getPaymentStatus()).isEqualTo("PAYE");
    }
}
