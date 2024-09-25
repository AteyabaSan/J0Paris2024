package com.joparis2024.dto;

import java.time.LocalDateTime;

public class PaymentDTO {
	
	private Long id; // ID du paiement
    private OrderDTO order; // Lien avec la commande
    private String paymentMethod; // Méthode de paiement
    private LocalDateTime paymentDate; // Date du paiement
    private double amount; // Montant du paiement
    private String paymentStatus; // Statut du paiement (en attente, échoué, réussi, etc.)

    // Constructeur sans paramètres
    public PaymentDTO() {
    }

    // Constructeur avec tous les paramètres
    public PaymentDTO(Long id, OrderDTO order, String paymentMethod, LocalDateTime paymentDate, double amount, String paymentStatus) {
        this.id = id;
    	this.order = order;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
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

