package com.joparis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.Order;
import com.joparis2024.model.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Order findByStripeSessionId(String stripeSessionId);
}
