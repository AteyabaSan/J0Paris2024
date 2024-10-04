package com.joparis2024.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TicketDTO {

    @NotNull
    private Long id;          // Ajout de l'ID
    
    @NotNull
    private EventDTO event;
    
    @NotNull
    private OrderDTO order;
    
    @Min(0)
    private double price;
    
    @Min(1)
    private int quantity;
    
    private boolean isAvailable; // Utilisation de 'isAvailable' au lieu de 'available'
    
    @NotNull
    private LocalDateTime eventDate;  // Ajout de la date de l'événement associé

    // Constructeurs
    public TicketDTO() {
    }

    public TicketDTO(Long id, EventDTO event, OrderDTO order, double price, int quantity, boolean isAvailable, LocalDateTime eventDate) {
        this.id = id;
        this.event = event;
        this.order = order;
        this.price = price;
        this.quantity = quantity;
        this.isAvailable = isAvailable;
        this.eventDate = eventDate;
    }

    // Getters and Setters
    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isAvailable() {  // Getter avec 'is'
        return isAvailable;
    }

    public void setAvailable(boolean available) {  // Setter sans 'is'
        this.isAvailable = available;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}