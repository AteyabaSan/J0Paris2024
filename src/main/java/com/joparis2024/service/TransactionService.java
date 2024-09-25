package com.joparis2024.service;

import com.joparis2024.dto.TransactionDTO;
import com.joparis2024.model.Transaction;
import com.joparis2024.model.Order;
import com.joparis2024.repository.TransactionRepository;
import com.joparis2024.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Récupérer une transaction par ID (Détails d'une transaction)
    public TransactionDTO getTransactionById(Long transactionId) throws Exception {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (!transaction.isPresent()) {
            throw new Exception("Transaction introuvable");
        }
        return mapToDTO(transaction.get());
    }

    // Récupérer toutes les transactions d'une commande (Lister les transactions)
    public List<TransactionDTO> getTransactionsByOrder(Long orderId) throws Exception {
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            throw new Exception("Commande introuvable");
        }

        List<Transaction> transactions = transactionRepository.findByOrder(order.get());
        return mapToDTOs(transactions);
    }

    // Mapper Transaction -> TransactionDTO
    public TransactionDTO mapToDTO(Transaction transaction) {
        return new TransactionDTO(
            transaction.getId(),
            transaction.getOrder().getId(),
            transaction.getTransactionType(),
            transaction.getTransactionStatus(),
            transaction.getTransactionDate()
        );
    }

    // Mapper une liste de Transactions -> Liste de TransactionDTOs
    public List<TransactionDTO> mapToDTOs(List<Transaction> transactions) {
        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionDTOs.add(mapToDTO(transaction));
        }
        return transactionDTOs;
    }
}
