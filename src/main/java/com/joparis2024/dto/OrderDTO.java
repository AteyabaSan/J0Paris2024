package com.joparis2024.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private String status;
    private UserDTO user;
    private List<TicketDTO> tickets;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private LocalDateTime paymentDate;

    // Constructeur sans argument
    public OrderDTO() {}

    // Constructeur avec tous les arguments
    public OrderDTO(String status, UserDTO user, List<TicketDTO> tickets, Double totalAmount, LocalDateTime orderDate, LocalDateTime paymentDate) {
        this.status = status;
        this.user = user;
        this.tickets = tickets;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.paymentDate = paymentDate;
    }

    // Getters et Setters
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

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}


