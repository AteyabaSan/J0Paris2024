package com.joparis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joparis2024.model.Order;
import com.joparis2024.service.EmailService;
import com.joparis2024.service.OrderService;

@RestController
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderService orderService;

    // Endpoint pour tester l'envoi d'email avec un ticket
    @GetMapping("/test-email-ticket")
    public String sendTestEmailWithTicket(@RequestParam Long orderId) {
        try {
            // Récupérer la commande par ID
            Order order = orderService.findOrderById(orderId);
            if (order == null) {
                return "Order not found!";
            }

            // Envoyer le ticket par email
            emailService.sendTicket(order);
            return "Email with ticket sent successfully to " + order.getUser().getEmail();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email with ticket.";
        }
    }
}
