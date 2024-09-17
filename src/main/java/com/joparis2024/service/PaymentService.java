package com.joparis2024.service;

import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.model.Order;
import com.joparis2024.model.Payment;
import com.joparis2024.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // Créer un paiement
    public Payment createPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setOrder(new Order(paymentDTO.getOrderId()));  // Récupérer l'objet Order
        payment.setAmount(paymentDTO.getAmount());
        payment.setStatus(paymentDTO.getStatus());
        return paymentRepository.save(payment);
    }

    // Récupérer un paiement par ID
    public Optional<PaymentDTO> getPaymentById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        return payment.map(this::mapToDTO);
    }

    // Mapper l'entité Payment vers un DTO PaymentDTO
    private PaymentDTO mapToDTO(Payment payment) {
        return new PaymentDTO(payment.getId(), payment.getOrder().getId(), payment.getAmount(), payment.getStatus());
    }
}
