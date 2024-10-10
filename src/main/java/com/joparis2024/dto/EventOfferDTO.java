package com.joparis2024.dto;

public class EventOfferDTO {
    
    private Long id;
    private Long eventId;   // Utilisation de l'ID de l'événement
    private Long offerId;   // Utilisation de l'ID de l'offre

    // Constructeurs
    public EventOfferDTO() {}

    public EventOfferDTO(Long id, Long eventId, Long offerId) {
        this.id = id;
        this.eventId = eventId;
        this.offerId = offerId;
    }

    // Getters et Setters
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

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }
}
