package com.joparis2024.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
@ActiveProfiles("test")
class PaymentTest {

    @Test
    void testPaymentGettersAndSetters() {
        Payment payment = new Payment();
        payment.setId(1L);
        Order order = new Order();
        payment.setOrder(order);
        payment.setPaymentStatus("PAID");
        payment.setPaymentMethod("Credit Card");
        LocalDateTime paymentDate = LocalDateTime.now();
        payment.setPaymentDate(paymentDate);

        assertEquals(1L, payment.getId());
        assertEquals(order, payment.getOrder());
        assertEquals("PAID", payment.getPaymentStatus());
        assertEquals("Credit Card", payment.getPaymentMethod());
        assertEquals(paymentDate, payment.getPaymentDate());
    }
}
