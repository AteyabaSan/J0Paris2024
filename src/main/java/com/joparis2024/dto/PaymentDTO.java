package com.joparis2024.dto;

import java.time.LocalDateTime;

public class PaymentDTO {
    private OrderDTO order;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private double amount;
    private boolean confirmed;

    // Constructeur sans paramètres
    public PaymentDTO() {
    }

    // Constructeur avec tous les paramètres
    public PaymentDTO(OrderDTO order, String paymentMethod, LocalDateTime paymentDate, double amount, boolean confirmed) {
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.confirmed = confirmed;
    }

    // Getters et setters
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

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}

