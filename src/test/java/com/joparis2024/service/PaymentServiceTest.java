package com.joparis2024.service;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.model.Order;
import com.joparis2024.model.Payment;
import com.joparis2024.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentService paymentService;

    private PaymentDTO paymentDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentMethod("Carte Bancaire");
        paymentDTO.setAmount(200.0);
        paymentDTO.setConfirmed(true);
        paymentDTO.setPaymentDate(java.time.LocalDateTime.now());
    }

    // Cas où la création de paiement réussit
    @Test
    public void createPayment_Success() {
    	when(orderService.mapToEntity(any(OrderDTO.class))).thenReturn(new Order());
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());

        Payment createdPayment = paymentService.createPayment(paymentDTO);

        assertNotNull(createdPayment);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    // Cas où la suppression de paiement réussit
    @Test
    public void cancelPayment_Success() throws Exception {
        Payment payment = new Payment();
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));

        paymentService.cancelPayment(1L);

        verify(paymentRepository, times(1)).delete(any(Payment.class));
    }

    // Cas où la suppression de paiement échoue
    @Test
    public void cancelPayment_Failure() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            paymentService.cancelPayment(1L);
        });

        String expectedMessage = "Le paiement n'existe pas";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
