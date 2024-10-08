package com.joparis2024.service;


import com.joparis2024.dto.TransactionDTO;
import com.joparis2024.mapper.TransactionMapper;
import com.joparis2024.model.Transaction;
import com.joparis2024.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(Long transactionId) throws Exception {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new Exception("Transaction introuvable"));

        return transactionMapper.toDTO(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getAllTransactions() throws Exception {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactionMapper.toDTOs(transactions);
    }

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) throws Exception {
        // Conversion du DTO en entité Transaction
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        
        // Sauvegarder la transaction dans la base de données
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Conversion de l'entité Transaction en DTO pour le retour
        return transactionMapper.toDTO(savedTransaction);
    }

    @Transactional
    public TransactionDTO updateTransaction(Long transactionId, TransactionDTO transactionDTO) throws Exception {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new Exception("Transaction introuvable"));

        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setTransactionStatus(transactionDTO.getTransactionStatus());
        transaction.setTransactionDate(transactionDTO.getTransactionDate());

        return transactionMapper.toDTO(transactionRepository.save(transaction));
    }

    @Transactional
    public void deleteTransaction(Long transactionId) throws Exception {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new Exception("Transaction introuvable"));
        transactionRepository.delete(transaction);
    }
}
