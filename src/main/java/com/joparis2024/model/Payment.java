package com.joparis2024.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class Payment {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID de paiement
	
    @ManyToOne(cascade = CascadeType.ALL)
    private Order order; // Lien avec la commande
    
    private String paymentMethod; // Méthode de paiement
    private LocalDateTime paymentDate; // Date et heure du paiement
    private double amount; // Montant du paiement
    private boolean confirmed; // Statut du paiement (confirmé ou non)
}
