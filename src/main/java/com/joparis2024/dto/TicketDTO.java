package com.joparis2024.dto;

public class TicketDTO {
	private Long id;          // Ajout de l'ID
    private EventDTO event;
    private OrderDTO order;
    private double price;
    private int quantity;
    private boolean available;

    // Constructeurs
    public TicketDTO() {
    }

    public TicketDTO(Long id, EventDTO event, OrderDTO order, double price, int quantity, boolean available) {
    	this.id = id;      // Ajout de l'ID dans le constructeur
    	this.event = event;
        this.order = order;
        this.price = price;
        this.quantity = quantity;
        this.available = available;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    
 // Getters et Setters pour l'ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
