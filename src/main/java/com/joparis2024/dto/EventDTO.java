package com.joparis2024.dto;

import java.time.LocalDate;
import java.util.List;

public class EventDTO {
    private Long id;  // Identifiant de l'événement
    private String eventName;  // Nom de l'événement
    private LocalDate eventDate;  // Date de l'événement
    private String description;  // Description de l'événement
    private Long categoryId;  // ID pour la catégorie
    private List<Long> ticketIds;  // Liste des IDs des tickets
    private List<Long> offerIds;  // Liste des IDs des offres

    // Constructeur sans arguments
    public EventDTO() {}

    // Constructeur avec tous les arguments
    public EventDTO(Long id, String eventName, LocalDate eventDate, String description, Long categoryId, List<Long> ticketIds, List<Long> offerIds) {
        this.id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.description = description;
        this.categoryId = categoryId;
        this.ticketIds = ticketIds;
        this.offerIds = offerIds;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(List<Long> ticketIds) {
        this.ticketIds = ticketIds;
    }

    public List<Long> getOfferIds() {
        return offerIds;
    }

    public void setOfferIds(List<Long> offerIds) {
        this.offerIds = offerIds;
    }
}
