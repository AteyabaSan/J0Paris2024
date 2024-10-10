package com.joparis2024.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = true)
    private Order order; // Lien avec la commande

    @Column(nullable = false)
    private String transactionType; // Type de transaction (ex : paiement, remboursement)

    @Column(nullable = false)
    private String transactionStatus; // Statut de la transaction (ex : réussi, échoué)

    @Column(nullable = false)
    private LocalDateTime transactionDate; // Date et heure de la transaction
}
