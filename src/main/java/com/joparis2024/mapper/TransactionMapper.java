package com.joparis2024.mapper;

import com.joparis2024.dto.TransactionDTO;
import com.joparis2024.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMapper {

    // Mapper Transaction -> TransactionDTO
    public TransactionDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("La transaction à mapper est nulle.");
        }

        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setOrderId(transaction.getOrder().getId());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setTransactionStatus(transaction.getTransactionStatus());
        dto.setTransactionDate(transaction.getTransactionDate());

        return dto;
    }

    // Mapper TransactionDTO -> Transaction
    public Transaction toEntity(TransactionDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Le TransactionDTO à mapper est nul.");
        }

        Transaction transaction = new Transaction();
        transaction.setId(dto.getId());
        // Il faut gérer l'Order ailleurs car on ne peut pas directement mapper l'ID de commande ici
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setTransactionStatus(dto.getTransactionStatus());
        transaction.setTransactionDate(dto.getTransactionDate());

        return transaction;
    }

    // Mapper une liste de Transactions -> Liste de TransactionDTOs
    public List<TransactionDTO> toDTOs(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalArgumentException("La liste des transactions est vide ou nulle.");
        }

        List<TransactionDTO> dtos = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les transactions en DTOs
        for (Transaction transaction : transactions) {
            dtos.add(toDTO(transaction));
        }

        return dtos;
    }

    // Mapper une liste de TransactionDTOs -> Liste de Transactions
    public List<Transaction> toEntities(List<TransactionDTO> transactionDTOs) {
        if (transactionDTOs == null || transactionDTOs.isEmpty()) {
            throw new IllegalArgumentException("La liste des TransactionDTOs est vide ou nulle.");
        }

        List<Transaction> transactions = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les DTOs en entités
        for (TransactionDTO dto : transactionDTOs) {
            transactions.add(toEntity(dto));
        }

        return transactions;
    }
}