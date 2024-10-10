package com.joparis2024.dto;

import java.util.List;

public class OfferDTO {
    private Long id;
    private String name;
    private int numberOfSeats;
    private List<Long> eventIds;  // IDs des événements

    // Suppression de la relation directe avec EventDTO pour les opérations basiques
    // Cette relation sera gérée dans la facade ou dans des méthodes plus complexes

    public OfferDTO() {}

    public OfferDTO(Long id, String name, int numberOfSeats, List<Long> eventIds) {
        this.id = id;
        this.name = name;
        this.numberOfSeats = numberOfSeats;
        this.eventIds = eventIds;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public List<Long> getEventIds() {
        return eventIds;
    }

    public void setEventIds(List<Long> eventIds) {
        this.eventIds = eventIds;
    }
}
