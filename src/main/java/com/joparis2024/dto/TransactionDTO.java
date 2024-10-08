package com.joparis2024.dto;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;
    private OrderSimpleDTO order; // Utilisation de OrderSimpleDTO à la place de l'ID
    private String transactionType; // Type de transaction (paiement, remboursement)
    private String transactionStatus; // Statut de la transaction
    private LocalDateTime transactionDate; // Date de la transaction

    // Constructeur sans arguments
    public TransactionDTO() {}

    // Constructeur avec tous les arguments
    public TransactionDTO(Long id, OrderSimpleDTO order, String transactionType, String transactionStatus, LocalDateTime transactionDate) {
        this.id = id;
        this.order = order;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.transactionDate = transactionDate;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderSimpleDTO getOrder() {
        return order;
    }

    public void setOrder(OrderSimpleDTO order) {
        this.order = order;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}