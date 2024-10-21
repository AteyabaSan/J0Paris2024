package com.joparis2024.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.joparis2024.model.Order;


import jakarta.transaction.Transactional;
@Service
public class PaymentManagementFacade {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StripeService stripeService;
    
    @Autowired
    private EmailService emailService;
    
   

    
    @Transactional
    public String initiatePayment(Long orderId) throws Exception {
        // Récupérer la commande à partir de l'ID
        Order order = orderService.findOrderById(orderId); // Récupérer l'entité Order
        if (order == null) {
            throw new Exception("Commande non trouvée");
        }

        // Créer la session Stripe en passant Order directement
        String stripeSessionId = stripeService.createStripePaymentSession(order);

        // Retourner l'ID de session Stripe pour rediriger l'utilisateur
        return stripeSessionId;
    }

    @Transactional
    public void confirmPayment(String stripeSessionId) {
        // Récupérer la commande via l'ID de session Stripe
        Order order = orderService.findByStripeSessionId(stripeSessionId);
        if (order != null) {
            // Mettre à jour la commande comme "PAIEMENT CONFIRMÉ"
            order.setStatus("PAIEMENT CONFIRMÉ");
            orderService.save(order);

            // Déclencher l'envoi des billets par email
            emailService.sendTicket(order);
        }
    }
}