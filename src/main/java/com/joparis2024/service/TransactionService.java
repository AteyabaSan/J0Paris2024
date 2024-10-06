package com.joparis2024.service;

import com.joparis2024.dto.OrderSimpleDTO;
import com.joparis2024.dto.TransactionDTO;
import com.joparis2024.mapper.TransactionMapper;
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

    @Autowired
    private TransactionMapper transactionMapper; // Injection du mapper

    // Récupérer une transaction par ID (Détails d'une transaction)
    public TransactionDTO getTransactionById(Long transactionId) throws Exception {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (!transaction.isPresent()) {
            throw new Exception("Transaction introuvable");
        }
        return transactionMapper.toDTO(transaction.get());
    }

    // Récupérer toutes les transactions d'une commande (Lister les transactions)
    public List<TransactionDTO> getTransactionsByOrder(Long orderId) throws Exception {
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            throw new Exception("Commande introuvable");
        }

        List<Transaction> transactions = transactionRepository.findByOrder(order.get());
        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionDTOs.add(transactionMapper.toDTO(transaction));
        }
        return transactionDTOs;
    }

    // Récupérer des transactions simplifiées d'une commande (utilisation d'OrderSimpleDTO)
    public List<TransactionDTO> getSimpleTransactionsByOrder(Long orderId) throws Exception {
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            throw new Exception("Commande introuvable");
        }

        List<Transaction> transactions = transactionRepository.findByOrder(order.get());
        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        for (Transaction transaction : transactions) {
            // Utilisation de TransactionDTO avec OrderSimpleDTO
            TransactionDTO transactionDTO = transactionMapper.toDTO(transaction);
            transactionDTO.setOrder(new OrderSimpleDTO(order.get().getId(), order.get().getStatus(), order.get().getTotalAmount()));  // Utilisation d'OrderSimpleDTO
            transactionDTOs.add(transactionDTO);
        }
        return transactionDTOs;
    }
}
