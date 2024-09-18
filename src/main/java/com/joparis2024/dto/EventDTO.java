package com.joparis2024.dto;

import java.time.LocalDateTime;

public class EventDTO {

    private String eventName;
    private LocalDateTime date;
    private String location;
    private String category;
    private double priceRange;
    private int availableTickets;
    private String description;
    private boolean isSoldOut;

    // No-Args Constructor
    public EventDTO() {}

    // All-Args Constructor
    public EventDTO(String eventName, LocalDateTime date, String location, String category, double priceRange, int availableTickets, String description, boolean isSoldOut) {
        this.eventName = eventName;
        this.date = date;
        this.location = location;
        this.category = category;
        this.priceRange = priceRange;
        this.availableTickets = availableTickets;
        this.description = description;
        this.isSoldOut = isSoldOut;
    }

    // Getters and Setters

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(double priceRange) {
        this.priceRange = priceRange;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }
}


