package com.joparis2024.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;  // Chaque ticket est lié à un événement

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;  // Chaque ticket peut être lié à une commande

    @Column(nullable = false)
    private double price;  // Prix du ticket

    @Column(nullable = false)
    private int quantity;  // Quantité disponible pour ce ticket

    @Column(nullable = false)
    private boolean isAvailable;  // Disponibilité du ticket

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;  // Date de l'événement associé à ce ticket
}

