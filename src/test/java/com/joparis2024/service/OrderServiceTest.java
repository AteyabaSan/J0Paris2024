package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.joparis2024.dto.OfferDTO;
import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.model.Order;
import com.joparis2024.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Test
    void testCreateOrder_Success() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        Order order = new Order();
        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(orderDTO);

        assertNotNull(result);
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).toDTO(order);
    }

    @Test
    void testUpdateOrder_Success() throws Exception {
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.updateOrder(orderId, orderDTO);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).toDTO(order);
    }

    @Test
    void testUpdateOrder_OrderNotFound() {
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> orderService.updateOrder(orderId, orderDTO));
        assertEquals("Commande non trouvée", exception.getMessage());
    }

    @Test
    void testGetAllOrders_Success() throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toDTOs(orders)).thenReturn(new ArrayList<>());

        List<OrderDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, times(1)).toDTOs(orders);
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        Long orderId = 1L;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        OrderDTO orderDTO = new OrderDTO();
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.getOrderById(orderId);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderMapper, times(1)).toDTO(order);
    }

    @Test
    void testGetOrderById_OrderNotFound() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(orderId));
        assertEquals("Commande non trouvée", exception.getMessage());
    }

    @Test
    void testCancelOrder_Success() throws Exception {
        Long orderId = 1L;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.cancelOrder(orderId);

        assertEquals("ANNULÉ", order.getStatus());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCancelOrder_OrderNotFound() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> orderService.cancelOrder(orderId));
        assertEquals("Commande non trouvée", exception.getMessage());
    }

    @Test
    void testFindByStripeSessionId_Success() {
        String stripeSessionId = "test_session_id";
        Order order = new Order();
        OrderDTO orderDTO = new OrderDTO();
        when(orderRepository.findByStripeSessionId(stripeSessionId)).thenReturn(order);
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.findByStripeSessionId(stripeSessionId);

        assertNotNull(result);
        verify(orderRepository, times(1)).findByStripeSessionId(stripeSessionId);
        verify(orderMapper, times(1)).toDTO(order);
    }

    @Test
    void testSaveOrder_Success() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        Order order = new Order();
        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.saveOrder(orderDTO);

        assertNotNull(result);
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).toDTO(order);
    }

    @Test
    void testCalculateTotalPrice_Success() {
        List<TicketDTO> tickets = new ArrayList<>();
        TicketDTO ticket1 = new TicketDTO();
        ticket1.setPrice(100.0);
        tickets.add(ticket1);
        TicketDTO ticket2 = new TicketDTO();
        ticket2.setPrice(150.0);
        tickets.add(ticket2);

        OfferDTO offer = new OfferDTO();
        offer.setName("Duo");

        double result = orderService.calculateTotalPrice(tickets, offer);

        assertEquals(500.0, result);
    }
}
