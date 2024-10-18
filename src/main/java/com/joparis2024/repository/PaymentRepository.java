package com.joparis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.Payment;
import com.joparis2024.model.Order;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrder(Order order);
    Payment findByPaymentIntentId(String paymentIntentId);
}
