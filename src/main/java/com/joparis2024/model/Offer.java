package com.joparis2024.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Ajout de l'id

    @Column(nullable = false)
    private String name;  // Nom de l'offre (Solo, Duo, Family, etc.)

    @Column(nullable = false)
    private int numberOfSeats;  // Nombre de sièges (1, 2, ou 4)

    // Relation avec Event - Many-to-Many
    @ManyToMany
    @JoinTable(
            name = "event_offer",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events;  // Liste des événements associés à l'offre

}
