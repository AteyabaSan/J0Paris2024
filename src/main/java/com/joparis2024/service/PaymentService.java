package com.joparis2024.service;

import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.mapper.PaymentMapper;
import com.joparis2024.model.Payment;
import com.joparis2024.repository.PaymentRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    @Lazy
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper; // Utilisation du OrderMapper

    @Autowired
    private PaymentMapper paymentMapper;

    @Transactional
    public Payment createPayment(PaymentDTO paymentDTO) throws Exception {
        if (paymentDTO.getOrder() == null) {
            throw new Exception("La commande ne peut pas être nulle lors de la création du paiement.");
        }

        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment.setOrder(orderMapper.toEntity(paymentDTO.getOrder())); // Utilisation de OrderMapper pour convertir OrderDTO en Order

        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(Long paymentId) throws Exception {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Le paiement n'existe pas"));

        Hibernate.initialize(payment.getOrder());

        return paymentMapper.toDTO(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllPayments() throws Exception {
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentDTO> paymentDTOs = new ArrayList<>();
        for (Payment payment : payments) {
            paymentDTOs.add(paymentMapper.toDTO(payment));
        }
        return paymentDTOs;
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
}
