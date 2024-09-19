package com.joparis2024.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Ajout de l'id avec auto-incrément
    private Long id;

    @Column(nullable = false)
    private String status;  // en attente, payé, confirmé

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private LocalDateTime paymentDate;
}
