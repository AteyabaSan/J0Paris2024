package com.joparis2024.dto;

import java.time.LocalDate;
import java.util.List;

public class EventDTO {
	
    private Long id;  // Ajout de l'id
    private String eventName;
    private LocalDate eventDate;
    private String description;
    private Long categoryId; // ID pour la catégorie
    private List<Long> ticketIds; // Liste des IDs des tickets
    
    private CategoryDTO category;  // Utiliser CategoryDTO ici
    private List<TicketDTO> tickets;  // Relation avec TicketDTO
    private UserDTO organizer;  // Relation avec UserDTO
    private List<OfferDTO> offers;  // Relation avec OfferDTO (ajouté)
    private List<Long> offerIds;
    
    // Constructeur sans arguments
    public EventDTO() {}

    // Constructeur avec tous les arguments
    public EventDTO(Long id, String eventName, LocalDate eventDate, String description, CategoryDTO category, List<TicketDTO> tickets, UserDTO organizer, List<OfferDTO> offers) {
    	
        this.id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.description = description;
        this.category = category;
        this.tickets = tickets;
        this.organizer = organizer;
        this.offers = offers;  // Ajout de l'attribut offers
    }

    // Getters et Setters
    
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // Getter et Setter pour ticketIds
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

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    public UserDTO getOrganizer() {
        return organizer;
    }

    public void setOrganizer(UserDTO organizer) {
        this.organizer = organizer;
    }

    public List<OfferDTO> getOffers() {
        return offers;  // Getter pour les offres
    }

    public void setOffers(List<OfferDTO> offers) {
        this.offers = offers;  // Setter pour les offres
    }
}
