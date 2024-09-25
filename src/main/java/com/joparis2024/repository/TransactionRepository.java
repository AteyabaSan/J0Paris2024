package com.joparis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.Transaction;
import com.joparis2024.model.Order;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByOrder(Order order); // Récupérer les transactions d'une commande
}
