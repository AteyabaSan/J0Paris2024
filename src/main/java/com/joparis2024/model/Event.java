package com.joparis2024.model;

import jakarta.persistence.*;
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
    private Long id;  // Ajout de l'ID

    @Column(nullable = false)
    private String eventName;  // Nom de l'événement

    @Column(nullable = false)
    private LocalDateTime eventDate;  // Date et heure de l'événement

    @Column(nullable = false)
    private String description;  // Description de l'événement

    // Relation avec Ticket: Un événement peut avoir plusieurs tickets
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    // Relation avec User: Un événement peut être organisé par un utilisateur (organisateur)
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    // Relation avec Category: Un événement appartient à une catégorie
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}

