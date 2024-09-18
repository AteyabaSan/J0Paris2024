package com.joparis2024.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ticket {

    @ManyToOne
    private Event event;

    @ManyToOne
    private Order order;

    private double price;

    private int quantity;

    private boolean isAvailable;
}
