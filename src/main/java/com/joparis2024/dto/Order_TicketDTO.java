package com.joparis2024.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Order_TicketDTO {

    private Long id;
    private Long orderId;
    private Long ticketId;
    
    @NotNull(message = "La quantité ne peut pas être nulle")
    @Min(value = 1, message = "La quantité doit être supérieure à zéro")
    private Integer quantity; // Quantité de tickets pour une commande
    
    private Long offerId;  // Ajouter l'offre ici
    
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
    
 // Constructeur avec tous les arguments (orderId, ticketId, quantity)
    public Order_TicketDTO(Long orderId, Long ticketId, Integer quantity) {
        this.orderId = orderId;
        this.ticketId = ticketId;
        this.quantity = quantity;
    }
    
 // Constructeur complet avec l'offre
    public Order_TicketDTO(Long orderId, Long ticketId, Integer quantity, Long offerId) {
        this.orderId = orderId;
        this.ticketId = ticketId;
        this.quantity = quantity;
        this.offerId = offerId;  // Initialisation de l'offre
    }

    // Getters et Setters
    
 // Getters et setters pour offerId
    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }
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
