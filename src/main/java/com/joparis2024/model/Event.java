package com.joparis2024.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Ajout de l'id
    private String eventName;
    private LocalDateTime date;
    private String location;
    private String category;
    private double priceRange;
    private int availableTickets;
    private String description;
    private boolean isSoldOut;
    
 // Relation avec Ticket: Un événement peut avoir plusieurs tickets
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    // Relation avec User: Un événement peut être organisé par un utilisateur (organisateur)
    @ManyToOne
    private User organizer;
}
