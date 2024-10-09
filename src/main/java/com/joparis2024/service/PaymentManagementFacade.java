package com.joparis2024.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.PaymentDTO;
//import com.joparis2024.mapper.PaymentMapper;
//import com.joparis2024.model.Payment;

@Service
public class PaymentManagementFacade {

    @Autowired
    private PaymentService paymentService;

//    @Autowired
//    private OrderService orderService;
    
//    @Autowired
//    private PaymentMapper paymentMapper;
//
//    public PaymentDTO createPaymentWithOrder(PaymentDTO paymentDTO) throws Exception {
//        // Vérification des informations de la commande avant de créer le paiement
//        if (paymentDTO.getOrder() == null) {
//            throw new Exception("La commande est nécessaire pour créer un paiement.");
//        }
//        
//        // Utilisation du service de commande pour vérifier l'existence de la commande
//        OrderDTO order = orderService.getOrderById(paymentDTO.getOrder().getId());
//        if (order == null) {
//            throw new Exception("Commande introuvable.");
//        }
//
//        // Création du paiement via PaymentService
//        Payment payment = paymentService.createPayment(paymentDTO);
//
//        // Conversion de l'entité Payment en PaymentDTO via PaymentMapper
//        return paymentMapper.toDTO(payment);
//    }


    public PaymentDTO getPaymentDetails(Long paymentId) throws Exception {
        // Récupérer les détails d'un paiement avec ses informations de commande
        return paymentService.getPaymentById(paymentId);
    }

    public List<PaymentDTO> getAllPayments() throws Exception {
        // Récupérer tous les paiements
        return paymentService.getAllPayments();
    }

    public PaymentDTO updatePayment(Long paymentId, PaymentDTO paymentDTO) throws Exception {
        // Mise à jour d'un paiement existant
        return paymentService.updatePayment(paymentId, paymentDTO);
    }

    public void cancelPayment(Long paymentId) throws Exception {
        // Annulation d'un paiement
        paymentService.cancelPayment(paymentId);
    }
}

