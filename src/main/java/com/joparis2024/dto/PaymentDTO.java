package com.joparis2024.dto;

import java.time.LocalDateTime;

public class PaymentDTO {
    
    private Long id; // ID du paiement
    private OrderSimpleDTO order; // Utilisation de OrderSimpleDTO à la place de OrderDTO
    private String paymentMethod; // Méthode de paiement
    private LocalDateTime paymentDate; // Date du paiement
    private double amount; // Montant du paiement
    private String paymentStatus; // Statut du paiement (en attente, échoué, réussi, etc.)
    
    
    // Informations de la carte
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;

    // Constructeur sans paramètres
    public PaymentDTO() {
    }

    // Constructeur avec tous les paramètres
    public PaymentDTO(Long id, OrderSimpleDTO order, String paymentMethod, LocalDateTime paymentDate, double amount, String paymentStatus) {
        this.id = id;
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }
    
 // Constructeur avec paramètres
    public PaymentDTO(Long id, OrderSimpleDTO order, Double amount, String paymentMethod, 
            String paymentStatus, LocalDateTime paymentDate, 
            String cardNumber, String expMonth, String expYear, String cvc) {
		this.id = id;
		this.order = order;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.paymentDate = paymentDate;
		this.cardNumber = cardNumber;
		this.expMonth = expMonth;
		this.expYear = expYear;
		this.cvc = cvc;
}
    
    // Getters et setters
    
 
    
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
    
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}