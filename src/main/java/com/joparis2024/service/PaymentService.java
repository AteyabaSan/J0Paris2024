package com.joparis2024.service;

import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.model.Payment;
import com.joparis2024.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    // Créer un nouveau paiement
    public Payment createPayment(PaymentDTO paymentDTO) throws Exception {
        if (paymentDTO.getOrder() == null) {
            throw new Exception("L'ordre ne peut pas être nul lors de la création du paiement.");
        }

        Payment payment = new Payment();
        payment.setOrder(orderService.mapToEntity(paymentDTO.getOrder()));
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setAmount(paymentDTO.getAmount());
        payment.setConfirmed(paymentDTO.isConfirmed());

        return paymentRepository.save(payment);
    }


    // Annuler un paiement (Suppression)
    public void cancelPayment(Long paymentId) throws Exception {
        try {
            Optional<Payment> payment = paymentRepository.findById(paymentId);
            if (!payment.isPresent()) {
                throw new Exception("Le paiement n'existe pas");
            }
            paymentRepository.delete(payment.get());
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'annulation du paiement: " + e.getMessage());
        }
    }

    // Obtenir tous les paiements
    public List<PaymentDTO> getAllPayments() throws Exception {
        try {
            List<Payment> payments = paymentRepository.findAll();
            List<PaymentDTO> paymentDTOs = new ArrayList<>();
            
            for (Payment payment : payments) {
                paymentDTOs.add(mapToDTO(payment));
            }
            
            return paymentDTOs;
        } catch (Exception e) {
            throw new Exception("Erreur lors de la récupération des paiements: " + e.getMessage());
        }
    }

    // Mapper PaymentDTO -> Payment
    public Payment mapToEntity(PaymentDTO paymentDTO) throws Exception {
        try {
            Payment payment = new Payment();
            payment.setId(paymentDTO.getId());  // Ajout de l'ID
            payment.setOrder(orderService.mapToEntity(paymentDTO.getOrder()));  // Mapper l'ordre
            payment.setPaymentMethod(paymentDTO.getPaymentMethod());
            payment.setPaymentDate(paymentDTO.getPaymentDate());
            payment.setAmount(paymentDTO.getAmount());
            payment.setConfirmed(paymentDTO.isConfirmed());

            return payment;
        } catch (Exception e) {
            throw new Exception("Erreur lors du mappage PaymentDTO -> Payment: " + e.getMessage());
        }
    }

 // Mapper Payment -> PaymentDTO
    public PaymentDTO mapToDTO(Payment payment) throws Exception {
        try {
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setId(payment.getId());  // Ajout de l'ID
            paymentDTO.setOrder(orderService.mapToDTO(payment.getOrder()));  // Mapper l'ordre
            paymentDTO.setPaymentMethod(payment.getPaymentMethod());
            paymentDTO.setPaymentDate(payment.getPaymentDate());
            paymentDTO.setAmount(payment.getAmount());
            paymentDTO.setConfirmed(payment.isConfirmed());

            return paymentDTO;
        } catch (Exception e) {
            throw new Exception("Erreur lors du mappage Payment -> PaymentDTO: " + e.getMessage());
        }
    }

}
