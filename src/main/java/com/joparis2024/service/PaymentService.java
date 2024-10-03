package com.joparis2024.service;

import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.model.Payment;
import com.joparis2024.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    @Lazy  // Injection différée pour éviter la boucle
    private OrderService orderService;

    // Créer un nouveau paiement (CREATE)
    public Payment createPayment(PaymentDTO paymentDTO) throws Exception {
        if (paymentDTO.getOrder() == null) {
            throw new Exception("La commande ne peut pas être nulle lors de la création du paiement.");
        }

        Payment payment = new Payment();
        payment.setOrder(orderService.mapToEntity(paymentDTO.getOrder()));
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus()); // Statut du paiement

        return paymentRepository.save(payment);
    }

    // Récupérer un paiement par son ID (READ)
    public PaymentDTO getPaymentById(Long paymentId) throws Exception {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            throw new Exception("Le paiement n'existe pas");
        }
        return mapToDTO(payment.get());
    }

    // Récupérer tous les paiements (READ)
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

    // Mettre à jour un paiement (UPDATE)
    public PaymentDTO updatePayment(Long paymentId, PaymentDTO paymentDTO) throws Exception {
        Optional<Payment> existingPayment = paymentRepository.findById(paymentId);
        if (!existingPayment.isPresent()) {
            throw new Exception("Le paiement n'existe pas");
        }

        Payment payment = existingPayment.get();
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus()); // Mise à jour du statut de paiement

        Payment updatedPayment = paymentRepository.save(payment);
        return mapToDTO(updatedPayment);
    }

    // Annuler un paiement (DELETE)
    public void cancelPayment(Long paymentId) throws Exception {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            throw new Exception("Le paiement n'existe pas");
        }
        paymentRepository.delete(payment.get());
    }

    // Mapper PaymentDTO -> Payment
    public Payment mapToEntity(PaymentDTO paymentDTO) throws Exception {
        Payment payment = new Payment();
        payment.setId(paymentDTO.getId());  // Ajout de l'ID
        payment.setOrder(orderService.mapToEntity(paymentDTO.getOrder()));  // Mapper la commande
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus()); // Statut de paiement

        return payment;
    }

    // Mapper Payment -> PaymentDTO
    public PaymentDTO mapToDTO(Payment payment) throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(payment.getId());  // Ajout de l'ID
        paymentDTO.setOrder(orderService.mapToDTO(payment.getOrder()));  // Mapper la commande
        paymentDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setPaymentStatus(payment.getPaymentStatus()); // Statut de paiement

        return paymentDTO;
    }
}

