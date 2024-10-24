package com.joparis2024.model;



import jakarta.persistence.*;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotNull;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = true)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
//    @NotNull(message = "Quantity ne doit pas être nulle")
//    @Min(value = 1, message = "Il doit y avoir au moins 1 ticket")
    private Integer quantity;

    @Column(name = "is_available", nullable = false)
    private boolean available;

    @Column(name = "event_date", nullable = true)
    private String eventDate;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "offer_id", nullable = false)  // Ajoute cette ligne pour gérer l'Offer
    private Offer offer;  // Relation avec l'Offer
    
    public Ticket(Long id) {
        this.id = id;
    }
}
