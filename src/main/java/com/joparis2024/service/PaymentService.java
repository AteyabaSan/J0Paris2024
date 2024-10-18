package com.joparis2024.service;

import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.mapper.PaymentMapper;
import com.joparis2024.model.Payment;
import com.joparis2024.repository.PaymentRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO) throws Exception {
        Payment payment = paymentMapper.toEntity(paymentDTO);
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDTO(savedPayment);  // Retourner un PaymentDTO
    }

    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(Long paymentId) throws Exception {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Le paiement n'existe pas"));
        return paymentMapper.toDTO(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllPayments() throws Exception {
        List<Payment> payments = paymentRepository.findAll();
        return paymentMapper.toDTOs(payments);
    }

    @Transactional
    public PaymentDTO updatePayment(Long paymentId, PaymentDTO paymentDTO) throws Exception {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Le paiement n'existe pas"));

        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());

        return paymentMapper.toDTO(paymentRepository.save(payment));
    }

    @Transactional
    public void cancelPayment(Long paymentId) throws Exception {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Le paiement n'existe pas"));
        paymentRepository.delete(payment);
    }

    @Transactional(readOnly = true)
    public PaymentDTO getSimplePaymentById(Long paymentId) throws Exception {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Le paiement n'existe pas"));

        // Logique simplifiée : ici, le mapper et les DTO prennent en charge la simplification des données
        return paymentMapper.toDTO(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> getSimplePayments() throws Exception {
        List<Payment> payments = paymentRepository.findAll();
        return paymentMapper.toDTOs(payments);
    }
    
    @Transactional
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }
    
    @Transactional
    public Payment updatePayment(Long paymentId, Payment payment) throws Exception {
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Paiement non trouvé."));
        
        existingPayment.setPaymentMethod(payment.getPaymentMethod());
        existingPayment.setPaymentDate(payment.getPaymentDate());
        existingPayment.setAmount(payment.getAmount());
        existingPayment.setPaymentStatus(payment.getPaymentStatus());

        return paymentRepository.save(existingPayment);
    }
    
    public Payment findByPaymentIntentId(String paymentIntentId) {
        return paymentRepository.findByPaymentIntentId(paymentIntentId);
    }
    
    @Transactional(readOnly = true)
    public Payment getPaymentEntityById(Long paymentId) throws Exception {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Le paiement n'existe pas"));
    }
}
