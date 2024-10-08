package com.joparis2024.dto;

public class Order_TicketDTO {

    private Long id;
    private Long orderId;
    private Long ticketId;
    private Integer quantity; // Quantité de tickets pour une commande

    // Constructeur sans argument
    public Order_TicketDTO() {}

    // Constructeur avec deux arguments (orderId et ticketId)
    public Order_TicketDTO(Long orderId, Long ticketId) {
        this.orderId = orderId;
        this.ticketId = ticketId;
    }

    // Constructeur avec tous les arguments
    public Order_TicketDTO(Long id, Long orderId, Long ticketId, Integer quantity) {
        this.id = id;
        this.orderId = orderId;
        this.ticketId = ticketId;
        this.quantity = quantity;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
