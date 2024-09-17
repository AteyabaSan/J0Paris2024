package com.joparis2024.dto;

public class TicketDTO {

    private Long id;
    private Long eventId;
    private Long ownerId;
    private Double price;
    private String seatNumber;
    private Boolean isAvailable;

    // Constructors
    public TicketDTO() {}

    public TicketDTO(Long id, Long eventId, Long ownerId, Double price, String seatNumber, Boolean isAvailable) {
        this.id = id;
        this.eventId = eventId;
        this.ownerId = ownerId;
        this.price = price;
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
