package com.joparis2024.service;

import com.joparis2024.config.StripeProperties;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StripeService {

    @Autowired
    private StripeProperties stripeProperties;

    // Initialisation de la clé secrète Stripe lors du démarrage de l'application
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeProperties.getSecretKey();
    }

    /**
     * Crée un PaymentIntent chez Stripe pour simuler un paiement
     * @param amount Le montant du paiement (en euros)
     * @param currency La devise (ex : EUR)
     * @return Le PaymentIntent créé
     * @throws StripeException En cas de problème avec Stripe
     */
    public PaymentIntent createPaymentIntent(Double amount, String currency) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100))  // Montant en centimes
                .setCurrency(currency)
                .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)  // Activation de la gestion automatique des méthodes de paiement
                        .build())
                .build();

        return PaymentIntent.create(params);
    }

    /**
     * Simule la confirmation d'un paiement pour un PaymentIntent donné avec les détails de la carte
     * @param paymentIntentId L'ID du PaymentIntent
     * @param cardNumber Le numéro de la carte
     * @param expMonth Le mois d'expiration de la carte
     * @param expYear L'année d'expiration de la carte
     * @param cvc Le code CVC de la carte
     * @return Le PaymentIntent mis à jour après la "confirmation"
     * @throws StripeException En cas de problème avec Stripe
     */
    public PaymentIntent confirmPayment(String paymentIntentId, String cardNumber, String expMonth, String expYear, String cvc) throws StripeException {
        // Créer une méthode de paiement avec les informations de la carte
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put("number", cardNumber);
        cardParams.put("exp_month", expMonth);
        cardParams.put("exp_year", expYear);
        cardParams.put("cvc", cvc);

        Map<String, Object> paymentMethodParams = new HashMap<>();
        paymentMethodParams.put("type", "card");
        paymentMethodParams.put("card", cardParams);

        // Créer un PaymentMethod sur Stripe
        PaymentMethod paymentMethod = PaymentMethod.create(paymentMethodParams);

        // Confirmer le PaymentIntent avec la méthode de paiement créée
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method", paymentMethod.getId());

        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        return paymentIntent.confirm(params);
    }


    /**
     * Vérifie l'état d'un PaymentIntent (utile pour obtenir des infos après une simulation)
     * @param paymentIntentId L'ID du PaymentIntent
     * @return L'état du PaymentIntent (ex : succeeded, requires_action, etc.)
     * @throws StripeException En cas de problème avec Stripe
     */
    public String getPaymentStatus(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        return paymentIntent.getStatus();  // Retourne l'état du PaymentIntent
    }
}
