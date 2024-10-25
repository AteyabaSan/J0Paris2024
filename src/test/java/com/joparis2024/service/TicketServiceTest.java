package com.joparis2024.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.zxing.WriterException;
import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.OfferDTO;
import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.TicketMapper;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.TicketRepository;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        // Setup logic if necessary
    }

    @Test
    void testCreateTicket_Success() throws Exception {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setQuantity(10);
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        // Act
        Ticket result = ticketService.createTicket(ticket);

        // Assert
        assertNotNull(result);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void testCreateTicket_InvalidQuantity() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setQuantity(0);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> ticketService.createTicket(ticket));
        assertEquals("La quantité doit être supérieure à 0.", exception.getMessage());
    }

    @Test
    void testUpdateTicket_Success() throws Exception {
        // Arrange
        Long ticketId = 1L;
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setQuantity(10);
        ticketDTO.setPrice(100.0);

        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setQuantity(5);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(ticketMapper.toDTO(ticket)).thenReturn(ticketDTO);

        // Act
        TicketDTO result = ticketService.updateTicket(ticketId, ticketDTO);

        // Assert
        assertNotNull(result);
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(1)).save(ticket);
        verify(ticketMapper, times(1)).toDTO(ticket);
    }

    @Test
    void testUpdateTicket_TicketNotFound() {
        // Arrange
        Long ticketId = 1L;
        TicketDTO ticketDTO = new TicketDTO();
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> ticketService.updateTicket(ticketId, ticketDTO));
        assertEquals("Ticket non trouvé", exception.getMessage());
    }

    @Test
    void testGetTicketById_Success() throws Exception {
        // Arrange
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        TicketDTO ticketDTO = new TicketDTO();
        when(ticketMapper.toDTO(ticket)).thenReturn(ticketDTO);

        // Act
        TicketDTO result = ticketService.getTicketById(ticketId);

        // Assert
        assertNotNull(result);
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketMapper, times(1)).toDTO(ticket);
    }

    @Test
    void testGetAllTickets_Success() throws Exception {
        // Arrange
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        when(ticketRepository.findAll()).thenReturn(tickets);
        when(ticketMapper.toDTOs(tickets)).thenReturn(new ArrayList<>());

        // Act
        List<TicketDTO> result = ticketService.getAllTickets();

        // Assert
        assertNotNull(result);
        verify(ticketRepository, times(1)).findAll();
        verify(ticketMapper, times(1)).toDTOs(tickets);
    }

    @Test
    void testDeleteTicket_Success() throws Exception {
        // Arrange
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        // Act
        ticketService.deleteTicket(ticketId);

        // Assert
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(1)).delete(ticket);
    }

    @Test
    void testDeleteTicket_TicketNotFound() {
        // Arrange
        Long ticketId = 1L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> ticketService.deleteTicket(ticketId));
        assertEquals("Ticket non trouvé", exception.getMessage());
    }

    @Test
    void testGenerateQRCode_Success() throws WriterException, IOException {
        // Act
        byte[] qrCode = ticketService.generateQRCode("test", 250, 250);

        // Assert
        assertNotNull(qrCode);
    }

    @Test
    void testProcessTicketAndSendEmail() throws WriterException, IOException {
        // Arrange
        OrderDTO demoOrder = new OrderDTO();

        // Utilisation de setters pour initialiser les propriétés
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventName("EventName");
        demoOrder.setEvent(eventDTO);

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setName("OfferName");
        demoOrder.setOffer(offerDTO);

        String email = "test@example.com";
        doNothing().when(emailService).sendTicketAsync(any(OrderDTO.class), eq(email), any(byte[].class));

        // Act
        ticketService.processTicketAndSendEmail(demoOrder, email);

        // Assert
        verify(emailService, times(1)).sendTicketAsync(eq(demoOrder), eq(email), any(byte[].class));
    }

}
