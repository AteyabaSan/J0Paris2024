package com.joparis2024.dto;

public class EventOfferDTO {
    
    private Long id;
    private EventDTO event;
    private OfferDTO offer;

    // Constructeurs
    public EventOfferDTO() {}

    public EventOfferDTO(Long id, EventDTO event, OfferDTO offer) {
        this.id = id;
        this.event = event;
        this.offer = offer;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
