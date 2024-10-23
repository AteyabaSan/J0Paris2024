package com.joparis2024.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private Long id;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ticket> tickets = new ArrayList<>();  // Initialisation de la liste des tickets

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id" , nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "event_offer",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "offer_id")
    )
    private List<Offer> offers = new ArrayList<>();  // Initialisation de la liste des offres
    
    @Column(name = "location")
    private String location ;
    
    // Méthode pour ajouter une offre à l'événement
    public void addOffer(Offer offer) {
        if (offer != null) {
            this.offers.add(offer);
        }
    }

    // Méthode pour ajouter un ticket à l'événement
    public void addTicket(Ticket ticket) {
        if (ticket != null) {
            this.tickets.add(ticket);
            ticket.setEvent(this);  // Associer l'événement au ticket
        }
    }
    
    public String getName() {
        return this.eventName;  // Supposons que l'attribut soit `name`.
    }

}
