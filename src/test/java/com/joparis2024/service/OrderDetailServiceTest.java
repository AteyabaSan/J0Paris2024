package com.joparis2024.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.model.Order;
import com.joparis2024.repository.OrderRepository;


import jakarta.persistence.EntityNotFoundException;


@ExtendWith(MockitoExtension.class)
public class OrderDetailServiceTest {

    @InjectMocks
    private OrderDetailService orderDetailService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    private Order order;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        // Initialisation de l'Order et de l'OrderDTO pour le test
        order = new Order();
        order.setId(1L);
        order.setStatus("EN_COURS");

        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setStatus("EN_COURS");
    }

    @Test
    void testGetOrderWithDetailsById_Success() throws Exception {
        // Mock du repository et du mapper
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        // Appel de la méthode
        OrderDTO result = orderDetailService.getOrderWithDetailsById(1L);

        // Vérifications
        assertNotNull(result);
        assertEquals(orderDTO.getId(), result.getId());
        assertEquals(orderDTO.getStatus(), result.getStatus());

        verify(orderRepository, times(1)).findById(1L);
        verify(orderMapper, times(1)).toDTO(order);
    }

    @Test
    void testGetOrderWithDetailsById_OrderNotFound() {
        // Mock du repository pour renvoyer une exception
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Appel de la méthode et vérification de l'exception
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            orderDetailService.getOrderWithDetailsById(1L);
        });

        assertEquals("Commande non trouvée", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderMapper, times(0)).toDTO(any(Order.class));  // Pas de mapping appelé
    }

    @Test
    void testGetAllOrdersWithDetails_Success() throws Exception {
        // Préparation d'une liste de commandes et DTO
        List<Order> orders = Arrays.asList(order);
        
        // Mock du repository et du mapper
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);
        
        // Appel de la méthode à tester
        List<OrderDTO> result = orderDetailService.getAllOrdersWithDetails();
        
        // Vérification du résultat
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderDTO, result.get(0));
        
        // Vérification des interactions avec les mocks
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, times(1)).toDTO(order);
    }

}
