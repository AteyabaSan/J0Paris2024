package com.joparis2024.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.OrderSimpleDTO;
import com.joparis2024.dto.TransactionDTO;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.mapper.TransactionMapper;
import com.joparis2024.model.Order;
import com.joparis2024.model.Transaction;
import com.joparis2024.repository.TransactionRepository;

@Service
public class TransactionManagementFacade {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private TransactionMapper transactionMapper;
    
    @Autowired
    private OrderMapper orderMapper;

    public TransactionDTO getTransactionDetails(Long transactionId) throws Exception {
        // Récupérer les détails complets d'une transaction
        return transactionService.getTransactionById(transactionId);
    }

    public List<TransactionDTO> getAllTransactionsForOrder(Long orderId) throws Exception {
        // Récupérer la commande via OrderService
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        if (orderDTO == null) {
            throw new Exception("Commande introuvable");
        }

        // Récupérer les transactions associées à cette commande
        Order order = orderMapper.toEntity(orderDTO);  // Convertir OrderDTO en entité Order
        List<Transaction> transactions = transactionRepository.findByOrder(order);

        // Convertir les transactions en TransactionDTO
        return transactionMapper.toDTOs(transactions);
    }

    public TransactionDTO createTransactionForOrder(Long orderId, TransactionDTO transactionDTO) throws Exception {
        // Création d'une transaction pour une commande
        OrderDTO order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new Exception("Commande introuvable.");
        }

        // Associer la commande à la transaction en utilisant OrderSimpleDTO
        transactionDTO.setOrder(new OrderSimpleDTO(order.getId(), order.getStatus(), order.getTotalAmount()));

        // Utiliser TransactionService pour créer la transaction
        return transactionService.createTransaction(transactionDTO);
    }

    public List<TransactionDTO> getSimpleTransactionsForOrder(Long orderId) throws Exception {
        // Récupérer la commande via OrderService
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        if (orderDTO == null) {
            throw new Exception("Commande introuvable");
        }

        // Récupérer les transactions associées à cette commande
        Order order = orderMapper.toEntity(orderDTO);
        List<Transaction> transactions = transactionRepository.findByOrder(order);

        // Transformer en TransactionDTO avec des informations simplifiées
        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDTO dto = transactionMapper.toDTO(transaction);
            dto.setOrder(new OrderSimpleDTO(order.getId(), order.getStatus(), order.getTotalAmount()));
            transactionDTOs.add(dto);
        }
        return transactionDTOs;
    }

}
