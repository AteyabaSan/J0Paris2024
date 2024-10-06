package com.joparis2024.mapper;

import org.springframework.stereotype.Component;

import com.joparis2024.dto.OrderSimpleDTO;
import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.model.Payment;
import com.joparis2024.model.Order;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentMapper {

    // Méthode de PaymentMapper pour utiliser OrderSimpleDTO
    private OrderSimpleDTO toOrderSimpleDTO(Order order) {
        return new OrderSimpleDTO(order.getId(), order.getStatus(), order.getTotalAmount());
    }

    // Convertir un Payment en PaymentDTO
    public PaymentDTO toDTO(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(payment.getId());
        paymentDTO.setOrder(payment.getOrder() != null ? toOrderSimpleDTO(payment.getOrder()) : null); // Utilisation de OrderSimpleDTO
        paymentDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setPaymentStatus(payment.getPaymentStatus());

        return paymentDTO;
    }

    // Convertir un PaymentDTO en Payment
    public Payment toEntity(PaymentDTO paymentDTO) {
        if (paymentDTO == null) {
            return null;
        }

        Payment payment = new Payment();
        payment.setId(paymentDTO.getId());
        payment.setOrder(paymentDTO.getOrder() != null ? toOrderEntity(paymentDTO.getOrder()) : null); // Conversion en entité Order
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());

        return payment;
    }

    // Convertir une liste de Payment en liste de PaymentDTO
    public List<PaymentDTO> toDTOs(List<Payment> payments) {
        if (payments == null || payments.isEmpty()) {
            return new ArrayList<>();
        }

        List<PaymentDTO> paymentDTOs = new ArrayList<>();
        for (Payment payment : payments) {
            paymentDTOs.add(toDTO(payment));
        }

        return paymentDTOs;
    }

    // Convertir une liste de PaymentDTO en liste de Payment
    public List<Payment> toEntities(List<PaymentDTO> paymentDTOs) {
        if (paymentDTOs == null || paymentDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Payment> payments = new ArrayList<>();
        for (PaymentDTO paymentDTO : paymentDTOs) {
            payments.add(toEntity(paymentDTO));
        }

        return payments;
    }

    // Méthode auxiliaire pour convertir OrderSimpleDTO en Order (si nécessaire)
    private Order toOrderEntity(OrderSimpleDTO orderSimpleDTO) {
        if (orderSimpleDTO == null) {
            return null;
        }
        
        Order order = new Order();
        order.setId(orderSimpleDTO.getId());
        order.setStatus(orderSimpleDTO.getStatus());
        order.setTotalAmount(orderSimpleDTO.getTotalAmount());

        return order;
    }
}
