package com.joparis2024.mapper;

import org.springframework.stereotype.Component;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.model.Payment;
import com.joparis2024.model.Order;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentMapper {

    // Convertir un Payment en PaymentDTO
    public PaymentDTO toDTO(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(payment.getId());
        paymentDTO.setOrder(payment.getOrder() != null ? toOrderDTO(payment.getOrder()) : null);
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
        payment.setOrder(paymentDTO.getOrder() != null ? toOrderEntity(paymentDTO.getOrder()) : null);
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
        // Utilisation d'une boucle classique pour transformer les entités en DTOs
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
        // Utilisation d'une boucle classique pour transformer les DTOs en entités
        for (PaymentDTO paymentDTO : paymentDTOs) {
            payments.add(toEntity(paymentDTO));
        }

        return payments;
    }

    // Méthodes auxiliaires pour convertir les commandes (Order et OrderDTO)
    private OrderDTO toOrderDTO(Order order) {
        // Méthode pour mapper l'entité Order vers un OrderDTO (à ajuster si nécessaire)
        return new OrderDTO(order.getId(), order.getStatus(), null, null, order.getTotalAmount(), order.getOrderDate(), null, null);
    }

    private Order toOrderEntity(OrderDTO orderDTO) {
        // Méthode pour mapper un OrderDTO vers l'entité Order (à ajuster si nécessaire)
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setStatus(orderDTO.getStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setOrderDate(orderDTO.getOrderDate());
        return order;
    }
}
