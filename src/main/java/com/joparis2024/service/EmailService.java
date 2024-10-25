package com.joparis2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.joparis2024.model.Order;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Méthode pour envoyer les tickets par email
    public void sendTicket(Order order) {
        // Récupère l'email de l'utilisateur connecté
        String email = customUserDetailsService.getCurrentUserEmail();

        // Construction de l'email
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Configurer les détails de l'email
            helper.setTo(email); // Utilise l'email récupéré de l'utilisateur connecté
            helper.setSubject("Vos billets pour l'événement " + order.getTickets().get(0).getEvent().getName()); // Objet de l'email
            helper.setText(buildEmailContent(order), true); // Corps de l'email (HTML)

            // Ajouter les pièces jointes si nécessaire (QR code, PDF, etc.)
            // helper.addAttachment("ticket.pdf", new File("path/to/ticket.pdf"));

            // Envoyer l'email
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace(); // Gérer les erreurs d'envoi d'email
        }
    }

    // Méthode pour construire le contenu HTML de l'email
    private String buildEmailContent(Order order) {
        StringBuilder content = new StringBuilder();
        content.append("<h1>Voici vos billets pour l'événement</h1>");
        content.append("<p>Événement : ").append(order.getTickets().get(0).getEvent().getName()).append("</p>");
        content.append("<p>Date : ").append(order.getOrderDate()).append("</p>");
        content.append("<p>Montant total : ").append(order.getTotalAmount()).append(" €</p>");
        content.append("<p>Merci pour votre achat !</p>");

        return content.toString();
    }
}
