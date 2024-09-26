package com.joparis2024.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import com.joparis2024.dto.EventDTO;
//import com.joparis2024.dto.TicketDTO;
//import com.joparis2024.model.Event;
//import com.joparis2024.model.Order;
//import com.joparis2024.model.Ticket;
//import com.joparis2024.repository.TicketRepository;
//
//@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTest {
//
//    @Mock
//    private TicketRepository ticketRepository;
//
//    @Mock
//    private EventService eventService;
//
//    @Mock
//    private OrderService orderService;
//
//    @InjectMocks
//    private TicketService ticketService;
//
//    private TicketDTO ticketDTO;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        ticketDTO = new TicketDTO();
//        ticketDTO.setPrice(50.0);
//        ticketDTO.setQuantity(2);
//        ticketDTO.setAvailable(true);
//    }
//
//    // Test pour la création de ticket avec succès
//    @Test
//    public void createTicket_Success() throws Exception {
//        // Simuler les mappages
//        EventDTO eventDTO = new EventDTO(); // Initialisation de l'EventDTO
//        eventDTO.setId(1L);  // Assigner un ID à l'événement pour éviter le NullPointerException
//        when(eventService.mapToEntity(any())).thenReturn(new Event());
//        when(orderService.mapToEntity(any())).thenReturn(new Order());
//
//        ticketDTO.setEvent(eventDTO); // Ajouter l'événement à ticketDTO pour éviter l'erreur
//
//        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());
//
//        Ticket createdTicket = ticketService.createTicket(ticketDTO);
//
//        assertNotNull(createdTicket);
//        verify(ticketRepository).save(any(Ticket.class));
//        verify(eventService).mapToEntity(any());
//        verify(orderService).mapToEntity(any());
//    }
//
//    // Test pour la mise à jour d'un ticket
//    @Test
//    public void updateTicket_Success() throws Exception {
//        Ticket existingTicket = new Ticket();
//        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(existingTicket));
//        when(eventService.mapToEntity(any())).thenReturn(new Event());
//        when(orderService.mapToEntity(any())).thenReturn(new Order());
//
//        when(ticketRepository.save(any(Ticket.class))).thenReturn(existingTicket);
//
//        Ticket updatedTicket = ticketService.updateTicket(1L, ticketDTO);
//
//        assertNotNull(updatedTicket);
//        verify(ticketRepository).save(any(Ticket.class));
//    }
//
//    // Test pour la mise à jour d'un ticket avec échec (ticket non trouvé)
//    @Test
//    public void updateTicket_Failure() {
//        when(ticketRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            ticketService.updateTicket(1L, ticketDTO);
//        });
//
//        assertEquals("Ticket non trouvé", exception.getMessage());
//    }
//    
//    //Test pour recuperation de Tickets
//    @Test
//    public void getAllTickets_Success() {
//        try {
//            // Simuler une liste de tickets
//            List<Ticket> tickets = new ArrayList<>();
//            tickets.add(new Ticket());
//
//            // Simuler le comportement du repository
//            when(ticketRepository.findAll()).thenReturn(tickets);
//
//            // Appel du service
//            List<TicketDTO> ticketDTOs = ticketService.getAllTickets();
//
//            // Assertions pour vérifier le résultat
//            assertNotNull(ticketDTOs);
//            assertEquals(1, ticketDTOs.size());
//
//            // Vérification des interactions avec le mock
//            verify(ticketRepository).findAll();
//        } catch (Exception e) {
//            // Si une exception est levée, échoue le test
//            fail("Une exception inattendue a été levée: " + e.getMessage());
//        }
//    }

}
