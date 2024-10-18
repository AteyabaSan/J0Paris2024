package com.joparis2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithTicket(String to, String subject, String body, byte[] qrCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        // Attacher le QR code en pièce jointe
        ByteArrayDataSource dataSource = new ByteArrayDataSource(qrCode, "image/png");
        helper.addAttachment("ticket.png", dataSource);

        mailSender.send(message);
    }
    
    public void sendSimpleEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        mailSender.send(message);
    }
}
