package com.joparis2024.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.service.PaymentManagementFacade;


@RestController
@RequestMapping("/api/payments")
public class PaymentManagementController {

    @Autowired
    private PaymentManagementFacade paymentManagementFacade;

    
    @PostMapping("/create")
    public ResponseEntity<?> createPaymentWithStripe(@RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentDTO createdPayment = paymentManagementFacade.createPaymentWithStripe(
                    paymentDTO.getOrder().getId(),
                    paymentDTO.getAmount(),
                    "EUR"
            );
            return ResponseEntity.ok(createdPayment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping("/confirm/{paymentId}")
    public ResponseEntity<?> confirmPayment(@PathVariable Long paymentId, @RequestBody Map<String, String> cardDetails) {
        try {
            String cardNumber = cardDetails.get("cardNumber");
            String expMonth = cardDetails.get("expMonth");
            String expYear = cardDetails.get("expYear");
            String cvc = cardDetails.get("cvc");

            // Appel à la méthode confirmPaymentWithStripe avec l'ID du paiement et les détails de la carte
            PaymentDTO confirmedPayment = paymentManagementFacade.confirmPaymentWithStripe(paymentId, cardNumber, expMonth, expYear, cvc);

            return ResponseEntity.ok(confirmedPayment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentDetails(@PathVariable Long paymentId) {
        try {
            PaymentDTO paymentDetails = paymentManagementFacade.getPaymentDetails(paymentId);
            return ResponseEntity.ok(paymentDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
