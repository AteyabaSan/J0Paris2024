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
    private List<TicketDTO> tickets;  // Liste des objets TicketDTO
    private List<Long> offerIds;  // Liste des IDs des offres
    private List<OfferDTO> offers;  // Liste des objets OfferDTO
    private String location;
    // Constructeur sans arguments
    public EventDTO() {}

    // Constructeur avec tous les arguments
    public EventDTO(String location, Long id, String eventName, LocalDate eventDate, String description, Long categoryId, List<Long> ticketIds, List<TicketDTO> tickets, List<Long> offerIds, List<OfferDTO> offers) {
        this.id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.description = description;
        this.categoryId = categoryId;
        this.ticketIds = ticketIds;
        this.tickets = tickets;
        this.offerIds = offerIds;
        this.offers = offers;
        this.location = location;
    }

    // Getters et Setters
    
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

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

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    public List<Long> getOfferIds() {
        return offerIds;
    }

    public void setOfferIds(List<Long> offerIds) {
        this.offerIds = offerIds;
    }

    public List<OfferDTO> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferDTO> offers) {
        this.offers = offers;
    }
}
