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

        // Utiliser les détails de la commande pour créer les articles à payer
        for (Ticket ticket : order.getTickets()) {
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("eur") // Mettre la devise à "eur"
                        .setUnitAmount((long) (ticket.getPrice() * 100)) // Montant en centimes
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(ticket.getEvent().getEventName()) // Nom de l'événement
                                .build()
                        )
                        .build()
                )
                .setQuantity((long) order.getTickets().size()) // Nombre de tickets dans la commande
                .build();

            lineItems.add(lineItem);
        }

        // Configurer la session Stripe
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("https://ton-domaine.com/success?session_id={CHECKOUT_SESSION_ID}")
            .setCancelUrl("https://ton-domaine.com/cancel")
            .addAllLineItem(lineItems)
            .build();

        // Créer la session Stripe
        Session session = Session.create(params);

        return session.getId(); // Retourner l'ID de session Stripe
    }
}
