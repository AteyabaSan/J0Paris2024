package com.joparis2024.service;

import java.io.IOException;

//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.model.Order;
import com.joparis2024.model.Ticket;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Méthode pour envoyer les tickets par email
    @Async
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
            helper.setText(buildEmailContent(order), true); // Utilise bien la méthode pour Order

            // Envoyer l'email
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Gérer les erreurs d'envoi d'email
        }
    }


    
    @Async
    public void sendTicketAsync(OrderDTO demoOrder, String email, byte[] qrCodeImage) throws IOException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Vos billets pour l'événement " + demoOrder.getEvent().getEventName());
            helper.setText(buildEmailContent(demoOrder), true);

            // Attacher le QR code à l'email
            ByteArrayDataSource dataSource = new ByteArrayDataSource(qrCodeImage, "image/png");
            helper.addAttachment("ticket-qr-code.png", dataSource);

            // Envoyer l'email
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Gérer les erreurs d'envoi d'email
        }
    }








    // Méthode pour construire le contenu HTML de l'email
    public String buildEmailContent(OrderDTO demoOrder) {
        StringBuilder content = new StringBuilder();
        content.append("<h1>Voici vos billets pour l'événement : ")
               .append(demoOrder.getEvent().getEventName())
               .append("</h1>");
        content.append("<p>Date de l'événement : ")
               .append(demoOrder.getEvent().getEventDate())
               .append("</p>");
        content.append("<p>Lieu : ")
               .append(demoOrder.getEvent().getLocation())
               .append("</p>");
        content.append("<h2>Vos tickets</h2><ul>");

        content.append("<li>").append(demoOrder.getOffer().getName())
               .append(" - Quantité : ").append(1).append("</li>");

        content.append("</ul>");
        content.append("<p>Scannez les QR codes ci-joints pour accéder à l'événement.</p>");
        
        return content.toString();
    }
    
 // Méthode pour construire le contenu HTML de l'email pour Order
    public String buildEmailContent(Order order) {
        StringBuilder content = new StringBuilder();
        content.append("<h1>Voici vos billets pour l'événement : ")
               .append(order.getTickets().get(0).getEvent().getName())
               .append("</h1>");
        content.append("<p>Date de l'événement : ")
               .append(order.getTickets().get(0).getEvent().getEventDate())
               .append("</p>");
        content.append("<p>Lieu : ")
               .append(order.getTickets().get(0).getEvent().getLocation())
               .append("</p>");
        content.append("<h2>Vos tickets</h2><ul>");

        for (Ticket ticket : order.getTickets()) {
            content.append("<li>").append(ticket.getOffer().getName())
                   .append(" - Quantité : ").append(ticket.getQuantity()).append("</li>");
        }

        content.append("</ul>");
        content.append("<p>Scannez les QR codes ci-joints pour accéder à l'événement.</p>");
        
        return content.toString();
    }



}
