package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joparis2024.dto.TransactionDTO;
import com.joparis2024.mapper.TransactionMapper;
import com.joparis2024.model.Transaction;
import com.joparis2024.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Test
    void testGetTransactionById_Success() throws Exception {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        TransactionDTO transactionDTO = new TransactionDTO();

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDTO(transaction)).thenReturn(transactionDTO);

        TransactionDTO result = transactionService.getTransactionById(transactionId);

        assertNotNull(result);
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionMapper, times(1)).toDTO(transaction);
    }

    @Test
    void testGetTransactionById_TransactionNotFound() {
        Long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> transactionService.getTransactionById(transactionId));
        assertEquals("Transaction introuvable", exception.getMessage());
    }

    @Test
    void testGetAllTransactions_Success() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());

        when(transactionRepository.findAll()).thenReturn(transactions);
        when(transactionMapper.toDTOs(transactions)).thenReturn(new ArrayList<>());

        List<TransactionDTO> result = transactionService.getAllTransactions();

        assertNotNull(result);
        verify(transactionRepository, times(1)).findAll();
        verify(transactionMapper, times(1)).toDTOs(transactions);
    }

    @Test
    void testCreateTransaction_Success() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        Transaction transaction = new Transaction();

        when(transactionMapper.toEntity(transactionDTO)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.toDTO(transaction)).thenReturn(transactionDTO);

        TransactionDTO result = transactionService.createTransaction(transactionDTO);

        assertNotNull(result);
        verify(transactionRepository, times(1)).save(transaction);
        verify(transactionMapper, times(1)).toDTO(transaction);
    }

    @Test
    void testUpdateTransaction_Success() throws Exception {
        Long transactionId = 1L;
        TransactionDTO transactionDTO = new TransactionDTO();
        Transaction transaction = new Transaction();

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.toDTO(transaction)).thenReturn(transactionDTO);

        TransactionDTO result = transactionService.updateTransaction(transactionId, transactionDTO);

        assertNotNull(result);
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).save(transaction);
        verify(transactionMapper, times(1)).toDTO(transaction);
    }

    @Test
    void testUpdateTransaction_TransactionNotFound() {
        Long transactionId = 1L;
        TransactionDTO transactionDTO = new TransactionDTO();

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> transactionService.updateTransaction(transactionId, transactionDTO));
        assertEquals("Transaction introuvable", exception.getMessage());
    }

    @Test
    void testDeleteTransaction_Success() throws Exception {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(transactionId);

        verify(transactionRepository, times(1)).delete(transaction);
    }

    @Test
    void testDeleteTransaction_TransactionNotFound() {
        Long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> transactionService.deleteTransaction(transactionId));
        assertEquals("Transaction introuvable", exception.getMessage());
    }
}
