package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.mapper.Order_TicketMapper;
import com.joparis2024.model.Order;
import com.joparis2024.model.Order_Ticket;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.OrderRepository;
import com.joparis2024.repository.Order_TicketRepository;
import com.joparis2024.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
@ExtendWith(MockitoExtension.class)
class Order_TicketServiceTest {

    @Mock
    private Order_TicketRepository orderTicketRepository;

    @Mock
    private Order_TicketMapper orderTicketMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private Order_TicketService orderTicketService;

    private Order_TicketDTO orderTicketDTO;
    private Order_Ticket orderTicket;
    private Order order;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup d'un exemple d'Order_TicketDTO et Order_Ticket
        orderTicketDTO = new Order_TicketDTO();
        orderTicketDTO.setOrderId(1L);
        orderTicketDTO.setTicketId(2L);
        orderTicketDTO.setQuantity(3);

        orderTicket = new Order_Ticket();
        orderTicket.setOrder(new Order());
        orderTicket.setTicket(new Ticket());
        orderTicket.setQuantity(3);

        order = new Order();
        order.setId(1L);

        ticket = new Ticket();
        ticket.setId(2L);
    }

    @Test
    void testCreateOrderTicket_Success() throws Exception {
        // Mock des repositories pour retourner un Order et Ticket existants
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(ticketRepository.findById(2L)).thenReturn(Optional.of(ticket));
        when(orderTicketMapper.toEntity(orderTicketDTO)).thenReturn(orderTicket);
        when(orderTicketRepository.save(orderTicket)).thenReturn(orderTicket);
        when(orderTicketMapper.toDTO(orderTicket)).thenReturn(orderTicketDTO);

        // Appel du service
        Order_TicketDTO result = orderTicketService.createOrderTicket(orderTicketDTO);

        // Vérifications
        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals(2L, result.getTicketId());
        assertEquals(3, result.getQuantity());

        verify(orderRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).findById(2L);
        verify(orderTicketRepository, times(1)).save(orderTicket);
    }

    @Test
    void testCreateOrderTicket_OrderNotFound() {
        // Mock pour retourner un Optional vide pour l'Order
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            orderTicketService.createOrderTicket(orderTicketDTO);
        });

        assertEquals("Commande non trouvée.", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
        verify(ticketRepository, times(0)).findById(anyLong());
    }

    @Test
    void testCreateOrderTicket_TicketNotFound() {
        // Mock pour retourner un Order mais pas de Ticket
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(ticketRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            orderTicketService.createOrderTicket(orderTicketDTO);
        });

        assertEquals("Ticket non trouvé.", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).findById(2L);
    }

    @Test
    void testDeleteOrderTicket_Success() throws Exception {
        // Mock pour simuler la suppression d'une association Order_Ticket
        when(orderTicketRepository.findById(1L)).thenReturn(Optional.of(orderTicket));

        // Appel du service
        orderTicketService.deleteOrderTicket(1L);

        // Vérification que le repository delete a été appelé
        verify(orderTicketRepository, times(1)).delete(orderTicket);
    }

    @Test
    void testDeleteOrderTicket_NotFound() {
        // Mock pour retourner un Optional vide
        when(orderTicketRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            orderTicketService.deleteOrderTicket(1L);
        });

        assertEquals("Relation Order-Ticket non trouvée", exception.getMessage());
        verify(orderTicketRepository, times(1)).findById(1L);
        verify(orderTicketRepository, times(0)).delete(any(Order_Ticket.class));
    }
}

