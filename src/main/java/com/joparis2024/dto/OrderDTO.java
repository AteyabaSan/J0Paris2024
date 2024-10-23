package com.joparis2024.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private Long id;
    private String status;
    private UserDTO user;
    private List<TicketDTO> tickets; // Relation Many-to-Many avec Ticket via Order_Ticket
    private Double totalAmount;
    private LocalDateTime orderDate;
    private PaymentDTO payment; // Relation One-to-One avec Payment
    private List<TransactionDTO> transactions; // Relation One-to-Many avec Transaction
    private EventDTO event; // Relation vers l'événement
    private OfferDTO offer; // Relation vers l'offre sélectionnée

    // Constructeur sans argument
    public OrderDTO() {}

    // Constructeur avec tous les arguments
    public OrderDTO(Long id, String status, UserDTO user, List<TicketDTO> tickets, Double totalAmount, LocalDateTime orderDate, PaymentDTO payment, List<TransactionDTO> transactions, EventDTO event, OfferDTO offer) {
        this.id = id;
        this.status = status;
        this.user = user;
        this.tickets = tickets;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.payment = payment;
        this.transactions = transactions;
        this.event = event;
        this.offer = offer;
    }

    // Getters et Setters pour tous les champs
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public OfferDTO getOffer() {
        return offer;
    }

    public void setOffer(OfferDTO offer) {
        this.offer = offer;
    }
}