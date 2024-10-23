package com.joparis2024.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.model.Order;
import com.joparis2024.dto.OrderDTO;


import jakarta.transaction.Transactional;
@Service
public class PaymentManagementFacade {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StripeService stripeService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private OrderMapper orderMapper;

    
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
        OrderDTO orderDTO = orderService.findByStripeSessionId(stripeSessionId);
        
        if (orderDTO != null) {
            // Mettre à jour la commande comme "PAIEMENT CONFIRMÉ"
            orderDTO.setStatus("PAIEMENT CONFIRMÉ");

            // Sauvegarder la commande mise à jour
            orderService.save(orderDTO);  // Sauvegarder en utilisant OrderDTO
            
            // Déclencher l'envoi des billets par email (envoie d'une entité)
            Order orderEntity = orderMapper.toEntity(orderDTO);  // Conversion en entité
            emailService.sendTicket(orderEntity);
        }
    }



}