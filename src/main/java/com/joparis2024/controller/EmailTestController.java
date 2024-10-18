package com.joparis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joparis2024.service.EmailService;

@RestController
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/test-email")
    public String sendTestEmail(@RequestParam String to) {
        try {
            String subject = "Test Email";
            String body = "This is a test email from your Spring Boot application!";
            emailService.sendSimpleEmail(to, subject, body);
            return "Email sent successfully to " + to;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email.";
        }
    }
}
