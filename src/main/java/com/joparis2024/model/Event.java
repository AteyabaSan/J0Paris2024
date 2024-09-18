package com.joparis2024.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private String eventName;
    private LocalDateTime date;
    private String location;
    private String category;
    private double priceRange;
    private int availableTickets;
    private String description;
    private boolean isSoldOut;

    // Autres méthodes si nécessaire
}
