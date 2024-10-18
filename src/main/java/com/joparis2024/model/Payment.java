package com.joparis2024.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID de paiement
    
    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Order order; // Lien avec la commande

    @Column(nullable = false)
    private String paymentMethod;  // Méthode de paiement

    @Column(nullable = false)
    private LocalDateTime paymentDate;  // Date et heure du paiement
    
    @Column(nullable = false)
    private Double amount;  // Montant du paiement

    @Column(nullable = false)
    private String paymentStatus;  // Statut du paiement (en attente, échoué, réussi, etc.)
   
    @Column(nullable = false)
    private String paymentIntentId;  // ID généré par Stripe pour le PaymentIntent
}

