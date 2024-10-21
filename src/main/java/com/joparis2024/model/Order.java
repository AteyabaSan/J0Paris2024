package com.joparis2024.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order_Ticket> orderTickets = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();
    
    @Column(name = "stripe_session_id")
    private String stripeSessionId;


    public Order(Long id) {
        this.id = id;
    }

    // Ajoute la méthode setOrderTickets si elle n'existe pas encore
    public void setOrderTickets(List<Order_Ticket> orderTickets) {
        this.orderTickets = orderTickets;
    }
    
    public List<Ticket> getTickets() {
        List<Ticket> tickets = new ArrayList<>();
        for (Order_Ticket orderTicket : this.orderTickets) {
            tickets.add(orderTicket.getTicket());
        }
        return tickets;
    }
}