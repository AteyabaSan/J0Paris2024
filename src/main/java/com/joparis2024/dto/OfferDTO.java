package com.joparis2024.dto;

import java.util.List;

public class OfferDTO {
    private Long id;
    private String name;
    private int numberOfSeats;

    // Nouvelle relation avec EventDTO
    private List<EventDTO> events;

    public OfferDTO() {}

    public OfferDTO(Long id, String name, int numberOfSeats, List<EventDTO> events) {
        this.id = id;
        this.name = name;
        this.numberOfSeats = numberOfSeats;
        this.events = events;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }

    public List<EventDTO> getEvents() { return events; }
    public void setEvents(List<EventDTO> events) { this.events = events; }
}
