package com.joparis2024.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EventDTO {
	
    private Long id;  // Ajout de l'id
    private String eventName;
    private LocalDateTime eventDate;
    private String description;
    
    private String category;  // Relation avec Category
    private List<TicketDTO> tickets;  // Relation avec TicketDTO
    private UserDTO organizer;  // Relation avec UserDTO

    // Constructeur sans arguments
    public EventDTO() {}

    // Constructeur avec tous les arguments
    public EventDTO(Long id, String eventName, LocalDateTime eventDate, String description, String category, List<TicketDTO> tickets, UserDTO organizer) {
    	
        this.id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.description = description;
        this.category = category;
        this.tickets = tickets;
        this.organizer = organizer;
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

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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
}



