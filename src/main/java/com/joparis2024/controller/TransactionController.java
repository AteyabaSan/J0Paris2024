package com.joparis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.joparis2024.dto.TransactionDTO;
import com.joparis2024.service.TransactionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    // Créer une transaction
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            logger.info("Requête pour créer une transaction");
            TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);
            return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la transaction", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer une transaction par ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        try {
            logger.info("Requête pour récupérer la transaction avec ID : {}", id);
            TransactionDTO transaction = transactionService.getTransactionById(id);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la transaction avec ID : {}", id, e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer toutes les transactions
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        try {
            logger.info("Requête pour récupérer toutes les transactions");
            List<TransactionDTO> transactions = transactionService.getAllTransactions();
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des transactions", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Mettre à jour une transaction
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO) {
        try {
            logger.info("Requête pour mettre à jour la transaction avec ID : {}", id);
            TransactionDTO updatedTransaction = transactionService.updateTransaction(id, transactionDTO);
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la transaction avec ID : {}", id, e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer une transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTransaction(@PathVariable Long id) {
        try {
            logger.info("Requête pour supprimer la transaction avec ID : {}", id);
            transactionService.deleteTransaction(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la transaction avec ID : {}", id, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

