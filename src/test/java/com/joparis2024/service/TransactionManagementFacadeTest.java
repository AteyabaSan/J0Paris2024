package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joparis2024.dto.OrderDTO;

import com.joparis2024.dto.TransactionDTO;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.mapper.TransactionMapper;
import com.joparis2024.model.Order;
import com.joparis2024.model.Transaction;
import com.joparis2024.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class TransactionManagementFacadeTest {

    @InjectMocks
    private TransactionManagementFacade transactionManagementFacade;

    @Mock
    private TransactionService transactionService;

    @Mock
    private OrderService orderService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private OrderMapper orderMapper;

    @Test
    void testGetTransactionDetails_Success() throws Exception {
        Long transactionId = 1L;
        TransactionDTO transactionDTO = new TransactionDTO();

        when(transactionService.getTransactionById(transactionId)).thenReturn(transactionDTO);

        TransactionDTO result = transactionManagementFacade.getTransactionDetails(transactionId);

        assertNotNull(result);
        verify(transactionService, times(1)).getTransactionById(transactionId);
    }

    @Test
    void testGetAllTransactionsForOrder_Success() throws Exception {
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        Order order = new Order();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());

        when(orderService.getOrderById(orderId)).thenReturn(orderDTO);
        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        when(transactionRepository.findByOrder(order)).thenReturn(transactions);
        when(transactionMapper.toDTOs(transactions)).thenReturn(new ArrayList<>());

        List<TransactionDTO> result = transactionManagementFacade.getAllTransactionsForOrder(orderId);

        assertNotNull(result);
        verify(orderService, times(1)).getOrderById(orderId);
        verify(orderMapper, times(1)).toEntity(orderDTO);
        verify(transactionRepository, times(1)).findByOrder(order);
        verify(transactionMapper, times(1)).toDTOs(transactions);
    }

    @Test
    void testGetAllTransactionsForOrder_OrderNotFound() throws Exception {
        Long orderId = 1L;

        when(orderService.getOrderById(orderId)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> transactionManagementFacade.getAllTransactionsForOrder(orderId));
        assertEquals("Commande introuvable", exception.getMessage());
    }


    @Test
    void testCreateTransactionForOrder_Success() throws Exception {
        Long orderId = 1L;
        TransactionDTO transactionDTO = new TransactionDTO();
        OrderDTO orderDTO = new OrderDTO();

        when(orderService.getOrderById(orderId)).thenReturn(orderDTO);
        when(transactionService.createTransaction(transactionDTO)).thenReturn(transactionDTO);

        TransactionDTO result = transactionManagementFacade.createTransactionForOrder(orderId, transactionDTO);

        assertNotNull(result);
        verify(orderService, times(1)).getOrderById(orderId);
        verify(transactionService, times(1)).createTransaction(transactionDTO);
    }

    @Test
    void testCreateTransactionForOrder_OrderNotFound() throws Exception {
        Long orderId = 1L;
        TransactionDTO transactionDTO = new TransactionDTO();

        when(orderService.getOrderById(orderId)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> transactionManagementFacade.createTransactionForOrder(orderId, transactionDTO));
        assertEquals("Commande introuvable.", exception.getMessage());
    }


    @Test
    void testGetSimpleTransactionsForOrder_Success() throws Exception {
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        Order order = new Order();
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transactions.add(transaction);

        when(orderService.getOrderById(orderId)).thenReturn(orderDTO);
        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        when(transactionRepository.findByOrder(order)).thenReturn(transactions);

        // Mocker toDTO() pour chaque transaction, au lieu de toDTOs()
        TransactionDTO transactionDTO = new TransactionDTO();
        when(transactionMapper.toDTO(transaction)).thenReturn(transactionDTO);

        List<TransactionDTO> result = transactionManagementFacade.getSimpleTransactionsForOrder(orderId);

        assertNotNull(result);
        assertEquals(1, result.size());  // Vérifier qu'une transaction a bien été transformée en DTO
        verify(orderService, times(1)).getOrderById(orderId);
        verify(orderMapper, times(1)).toEntity(orderDTO);
        verify(transactionRepository, times(1)).findByOrder(order);
        verify(transactionMapper, times(1)).toDTO(transaction);  // Vérifier que l'appel à toDTO() a été effectué pour chaque transaction
    }


    @Test
    void testGetSimpleTransactionsForOrder_OrderNotFound() throws Exception {
        Long orderId = 1L;

        when(orderService.getOrderById(orderId)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> transactionManagementFacade.getSimpleTransactionsForOrder(orderId));
        assertEquals("Commande introuvable", exception.getMessage());
    }

}
