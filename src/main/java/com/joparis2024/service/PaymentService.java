package com.joparis2024.service;

import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.model.Order;
import com.joparis2024.model.Payment;
import com.joparis2024.repository.PaymentRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

	 @PersistenceContext
	    private EntityManager entityManager;  // Pour utiliser le merge

	    @Autowired
	    private PaymentRepository paymentRepository;

	    @Autowired
	    @Lazy
	    private OrderService orderService;

	    // Créer un nouveau paiement (CREATE)
	    @Transactional
	    public Payment createPayment(PaymentDTO paymentDTO) throws Exception {
	        if (paymentDTO.getOrder() == null) {
	            throw new Exception("La commande ne peut pas être nulle lors de la création du paiement.");
	        }

	        Payment payment;
	        if (paymentDTO.getId() != null) {
	            // Si l'ID du paiement est présent, on récupère le paiement existant
	            payment = entityManager.find(Payment.class, paymentDTO.getId());
	            if (payment == null) {
	                throw new Exception("Le paiement avec l'ID " + paymentDTO.getId() + " n'existe pas.");
	            }
	            // On merge l'entité détachée
	            payment = entityManager.merge(payment);
	        } else {
	            // Sinon on crée un nouveau paiement
	            payment = new Payment();
	        }

	        payment.setOrder(orderService.mapToEntity(paymentDTO.getOrder()));
	        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
	        payment.setPaymentDate(paymentDTO.getPaymentDate());
	        payment.setAmount(paymentDTO.getAmount());
	        payment.setPaymentStatus(paymentDTO.getPaymentStatus());

	        return paymentRepository.save(payment);
	    }
	

    // Récupérer un paiement par son ID (READ)
	@Transactional(readOnly = true)
    public PaymentDTO getPaymentById(Long paymentId) throws Exception {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            throw new Exception("Le paiement n'existe pas");
        }
        return mapToDTO(payment.get());
    }

    // Récupérer tous les paiements (READ)
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public void cancelPayment(Long paymentId) throws Exception {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            throw new Exception("Le paiement n'existe pas");
        }
        paymentRepository.delete(payment.get());
    }

    @Transactional(readOnly = true)
    public Payment mapToEntity(PaymentDTO paymentDTO) throws Exception {
        if (paymentDTO == null) {
            throw new Exception("Le PaymentDTO est nul.");
        }

        Payment payment = new Payment();
        payment.setId(paymentDTO.getId());  // Ajout de l'ID

        // Vérification et récupération de la commande associée
        if (paymentDTO.getOrder() == null || paymentDTO.getOrder().getId() == null) {
            System.err.println("Order dans PaymentDTO est nul ou incomplet : " + paymentDTO);
            throw new Exception("Le paiement nécessite une commande valide.");
        }

        // Utilisation de l'EntityManager pour gérer l'état de la commande
        Order order = entityManager.find(Order.class, paymentDTO.getOrder().getId());
        if (order == null) {
            System.err.println("Commande introuvable pour l'ID : " + paymentDTO.getOrder().getId());
            throw new Exception("Commande non trouvée pour le paiement.");
        }

        // Log des autres champs pour déboguer
        System.out.println("Méthode de paiement : " + paymentDTO.getPaymentMethod());
        System.out.println("Date de paiement : " + paymentDTO.getPaymentDate());
        System.out.println("Montant : " + paymentDTO.getAmount());
        System.out.println("Statut du paiement : " + paymentDTO.getPaymentStatus());

        payment.setOrder(order);
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());  // Statut de paiement

        return payment;
    }



  // Mapper Payment -> PaymentDTO
    @Transactional(readOnly = true)
    public PaymentDTO mapToDTO(Payment payment) throws Exception {
     PaymentDTO paymentDTO = new PaymentDTO();
     paymentDTO.setId(payment.getId());
     paymentDTO.setOrder(orderService.mapToDTO(payment.getOrder()));
     paymentDTO.setPaymentMethod(payment.getPaymentMethod());
     paymentDTO.setPaymentDate(payment.getPaymentDate());
     paymentDTO.setAmount(payment.getAmount());
     paymentDTO.setPaymentStatus(payment.getPaymentStatus());

     return paymentDTO;
 }
}

