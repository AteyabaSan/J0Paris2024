package com.joparis2024.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.Event;
import com.joparis2024.service.PaymentManagementFacade;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/api/payments")
public class PaymentManagementController {

    @Autowired
    private PaymentManagementFacade paymentManagementFacade;

    // Endpoint pour initier le paiement avec Stripe une fois que la commande est créée
    @PostMapping("/create")
    public ResponseEntity<String> initiatePayment(@RequestParam Long orderId) {
        try {
            // Appel à la méthode de la facade pour initier le paiement avec Stripe
            String stripeSessionId = paymentManagementFacade.initiatePayment(orderId);
            return ResponseEntity.ok(stripeSessionId); // Renvoie l'ID de session Stripe
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    @PostMapping("/payment/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        String endpointSecret = "whsec_Bkihh36f8MlE5OlEUCdfFdrpnEZt1OZd"; // Définis ton secret ici
        
        try {
            // Vérifier la signature du webhook
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            // Traiter les différents types d'événements Stripe
            switch (event.getType()) {
                case "checkout.session.completed":
                    // Gérer la confirmation du paiement ici
                    break;
                case "payment_intent.succeeded":
                    // Gérer la réussite du paiement ici
                    break;
                case "charge.refunded":
                    // Gérer le remboursement ici
                    break;
                // Ajoute plus de cas selon tes besoins
                default:
                    System.out.println("Événement non pris en charge : " + event.getType());
            }

            return ResponseEntity.ok("Événement reçu avec succès");
        } catch (SignatureVerificationException e) {
            // Si la signature du webhook est incorrecte, retournez une erreur
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature non vérifiée");
        }
    }

}
