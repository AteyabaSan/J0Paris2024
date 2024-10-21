package com.joparis2024.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public class TicketDTO {

    
    private Long id;          // Ajout de l'ID
    
    @NotNull(message = "L'événement est obligatoire")
    private EventDTO event;

    @NotNull(message = "La commande est obligatoire")
    private OrderDTO order;
    
    @NotNull(message = "Le prix est obligatoire")
    private Double price;
    
    @NotNull(message = "Quantity ne peut pas etre nulle")
    @Min(value = 1, message = "La quantité doit être supérieure à zéro")
    private Integer quantity;

    
    private boolean available; // Utilisation de 'isAvailable' au lieu de 'available'
    
    @NotNull
    private String eventDate;  // Ajout de la date de l'événement associé

 // Nouvel attribut pour l'offre
    private Long offerId;
    
    private String eventName;
    
    // Constructeurs
    public TicketDTO() {
    }

    public TicketDTO(Long id, EventDTO event, OrderDTO order, Double price, Integer quantity, boolean available, String eventDate, Long offerId, String eventName) {
        this.id = id;
        this.event = event;
        this.order = order;
        this.price = price;
        this.quantity = quantity;
        this.available = available;
        this.eventDate = eventDate;
        this.offerId = offerId;
        this.eventName = eventName;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isAvailable() {  // Getter avec 'is'
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    // Nouvelle méthode pour gérer l'offre
    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }
    
 // Ajoute un getter pour eventName
    public String getEventName() {
        return eventName;
    }
    
    // Et un setter si nécessaire
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}