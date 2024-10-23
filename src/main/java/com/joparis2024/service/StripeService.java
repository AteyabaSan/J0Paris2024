package com.joparis2024.service;


import com.joparis2024.model.Order;
import com.joparis2024.model.Ticket;
import com.stripe.exception.StripeException;

import com.stripe.Stripe;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;


@Service
public class StripeService {

    @Value("${stripe.api.key.secret-key}")
    private String stripeApiKey;

    public String createStripePaymentSession(Order order) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        // Créer un article pour chaque ticket de la commande
        for (Ticket ticket : order.getTickets()) {
            lineItems.add(
                SessionCreateParams.LineItem.builder()
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("eur")
                            .setUnitAmount((long) (ticket.getPrice() * 100))
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(ticket.getEvent().getEventName())
                                    .build()
                            )
                            .build()
                    )
                    .setQuantity(1L)
                    .build()
            );
        }

        // Configurer la session Stripe
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:8081/payment/success?session_id={CHECKOUT_SESSION_ID}")
            .setCancelUrl("http://localhost:8081/payment/cancel")
            .addAllLineItem(lineItems)
            .build();

        // Créer la session Stripe
        Session session = Session.create(params);

        // Vérifier si la session Stripe est bien créée
        System.out.println("Stripe session created with ID: " + session.getId());

        return session.getId();  // Retourner l'ID de session Stripe
    }
}
