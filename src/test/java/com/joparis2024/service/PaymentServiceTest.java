package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.mapper.PaymentMapper;
import com.joparis2024.model.Payment;
import com.joparis2024.repository.PaymentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @BeforeEach
    void setUp() {
        // Setup if needed
    }

    @Test
    void testCreatePayment_Success() throws Exception {
        // Arrange
        PaymentDTO paymentDTO = new PaymentDTO();
        Payment payment = new Payment();
        when(paymentMapper.toEntity(paymentDTO)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(paymentDTO);

        // Act
        PaymentDTO result = paymentService.createPayment(paymentDTO);

        // Assert
        assertNotNull(result);
        verify(paymentRepository, times(1)).save(payment);
        verify(paymentMapper, times(1)).toDTO(payment);
    }

    @Test
    void testGetPaymentById_Success() throws Exception {
        // Arrange
        Long paymentId = 1L;
        Payment payment = new Payment();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        PaymentDTO paymentDTO = new PaymentDTO();
        when(paymentMapper.toDTO(payment)).thenReturn(paymentDTO);

        // Act
        PaymentDTO result = paymentService.getPaymentById(paymentId);

        // Assert
        assertNotNull(result);
        verify(paymentRepository, times(1)).findById(paymentId);
        verify(paymentMapper, times(1)).toDTO(payment);
    }

    @Test
    void testGetPaymentById_PaymentNotFound() {
        // Arrange
        Long paymentId = 1L;
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> paymentService.getPaymentById(paymentId));
        assertEquals("Le paiement n'existe pas", exception.getMessage());
    }

    @Test
    void testGetAllPayments_Success() throws Exception {
        // Arrange
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment());
        when(paymentRepository.findAll()).thenReturn(payments);
        when(paymentMapper.toDTOs(payments)).thenReturn(new ArrayList<>());

        // Act
        List<PaymentDTO> result = paymentService.getAllPayments();

        // Assert
        assertNotNull(result);
        verify(paymentRepository, times(1)).findAll();
        verify(paymentMapper, times(1)).toDTOs(payments);
    }

    @Test
    void testUpdatePayment_Success() throws Exception {
        // Arrange
        Long paymentId = 1L;
        PaymentDTO paymentDTO = new PaymentDTO();
        Payment payment = new Payment();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(paymentDTO);

        // Act
        PaymentDTO result = paymentService.updatePayment(paymentId, paymentDTO);

        // Assert
        assertNotNull(result);
        verify(paymentRepository, times(1)).findById(paymentId);
        verify(paymentRepository, times(1)).save(payment);
        verify(paymentMapper, times(1)).toDTO(payment);
    }

    @Test
    void testUpdatePayment_PaymentNotFound() {
        // Arrange
        Long paymentId = 1L;
        PaymentDTO paymentDTO = new PaymentDTO();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> paymentService.updatePayment(paymentId, paymentDTO));
        assertEquals("Le paiement n'existe pas", exception.getMessage());
    }

    @Test
    void testCancelPayment_Success() throws Exception {
        // Arrange
        Long paymentId = 1L;
        Payment payment = new Payment();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // Act
        paymentService.cancelPayment(paymentId);

        // Assert
        verify(paymentRepository, times(1)).findById(paymentId);
        verify(paymentRepository, times(1)).delete(payment);
    }

    @Test
    void testCancelPayment_PaymentNotFound() {
        // Arrange
        Long paymentId = 1L;
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> paymentService.cancelPayment(paymentId));
        assertEquals("Le paiement n'existe pas", exception.getMessage());
    }

    @Test
    void testFindByPaymentIntentId_Success() {
        // Arrange
        String paymentIntentId = "test_intent_id";
        Payment payment = new Payment();
        when(paymentRepository.findByPaymentIntentId(paymentIntentId)).thenReturn(payment);

        // Act
        Payment result = paymentService.findByPaymentIntentId(paymentIntentId);

        // Assert
        assertNotNull(result);
        verify(paymentRepository, times(1)).findByPaymentIntentId(paymentIntentId);
    }
}
