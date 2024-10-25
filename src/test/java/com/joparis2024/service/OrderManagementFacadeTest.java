package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joparis2024.dto.OfferDTO;
import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.dto.UserDTO;
import com.joparis2024.mapper.EventMapper;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.model.Offer;
import com.joparis2024.model.Order;
import com.joparis2024.model.Role;
import com.joparis2024.model.Ticket;
import com.joparis2024.model.User;
import com.joparis2024.model.UserRole;
import com.joparis2024.repository.OfferRepository;
import com.joparis2024.repository.OrderRepository;
import com.joparis2024.repository.TicketRepository;
import com.joparis2024.repository.UserRepository;
import com.joparis2024.repository.UserRoleRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderManagementFacadeTest {

    @InjectMocks
    private OrderManagementFacade orderManagementFacade;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @Mock
    private Order_TicketService orderTicketService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    public OrderMapper orderMapper;

    @Mock
    private OrderDetailService orderDetailService;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private EmailService emailService;

    private OrderDTO orderDTO;
    private UserDTO userDTO;  // Utiliser UserDTO
    private TicketDTO ticketDTO;  // Utiliser TicketDTO
    private OfferDTO offerDTO;  // Utiliser OfferDTO
    @BeforeEach
    void setUp() {
        // Initialisation de l'OrderDTO, UserDTO, TicketDTO, OfferDTO pour les tests
        orderDTO = new OrderDTO();
        userDTO = new UserDTO();  // Utilisation correcte de UserDTO
        userDTO.setId(1L);  // Assigner un ID
        userDTO.setEmail("test@example.com");  // Assigner un email
        orderDTO.setUser(userDTO);  // Assigner le UserDTO à OrderDTO

        ticketDTO = new TicketDTO();  // Utilisation correcte de TicketDTO
        ticketDTO.setId(1L);  // Assigner un ID à TicketDTO
        ticketDTO.setPrice(50.0);  // Assigner un prix

        offerDTO = new OfferDTO();  // Utilisation correcte de OfferDTO
        offerDTO.setId(1L);  // Assigner un ID à OfferDTO

        // Mock des rôles de l'utilisateur
        UserRole userRole = new UserRole();
        Role role = new Role();
        role.setName("USER");
        userRole.setRole(role);

        lenient().when(userRoleRepository.findByUser(any(User.class))).thenReturn(Collections.singletonList(userRole));

    }


    @Test
    void testCreateOrderWithDetails_Success() throws Exception {
        // Mock de l'utilisateur
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        
        // Mock la réponse de userRepository.findByEmail() pour renvoyer l'utilisateur
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Crée le UserDTO pour l'OrderDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        orderDTO.setUser(userDTO);

        // Mock de l'offre
        Offer offer = new Offer();
        offer.setId(1L);
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        // Mock du ticket
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setPrice(50.0);
        ticket.setAvailable(true);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        // Crée le TicketDTO pour l'OrderDTO
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(1L);  // ID correspond à l'ID du ticket mocké
        ticketDTO.setQuantity(1);
        ticketDTO.setPrice(50.0);
        ticketDTO.setOfferId(offer.getId());
        orderDTO.setTickets(Collections.singletonList(ticketDTO));

        // Mock de la méthode createOrder
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(orderDTO);

        // Exécution du test
        OrderDTO result = orderManagementFacade.createOrderWithDetails(orderDTO);

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(50.0, result.getTotalAmount());
        verify(orderService, times(1)).createOrder(any(OrderDTO.class));
        verify(ticketRepository, times(1)).findById(1L);  // Vérifie que le ticket a bien été récupéré
    }


    @Test
    void testCreateOrderWithDetails_UserNotFound() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUser(userDTO);

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            orderManagementFacade.createOrderWithDetails(orderDTO);
        });

        assertEquals("Utilisateur non trouvé.", exception.getMessage());
    }


    @Test
    void testUpdateOrderWithDetails_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUser(userDTO);  // Utilisation de UserDTO

        when(orderService.updateOrder(1L, orderDTO)).thenReturn(orderDTO);
        when(userService.updateUserByEmail(anyString(), any(UserDTO.class))).thenReturn(userDTO);

        OrderDTO updatedOrder = orderManagementFacade.updateOrderWithDetails(1L, orderDTO);

        assertNotNull(updatedOrder);
        verify(orderService, times(1)).updateOrder(1L, orderDTO);
        verify(userService, times(1)).updateUserByEmail(anyString(), any(UserDTO.class));
    }


    @Test
    void testCancelOrderWithDetails_Success() throws Exception {
        List<Order_TicketDTO> orderTickets = new ArrayList<>();
        Order_TicketDTO orderTicketDTO = new Order_TicketDTO();
        orderTicketDTO.setId(1L);
        orderTickets.add(orderTicketDTO);

        when(orderTicketService.getOrderTicketsByOrder(1L)).thenReturn(orderTickets);

        orderManagementFacade.cancelOrderWithDetails(1L);

        verify(orderTicketService, times(1)).getOrderTicketsByOrder(1L);
        verify(orderTicketService, times(1)).deleteOrderTicket(orderTicketDTO.getId());
        verify(orderService, times(1)).cancelOrder(1L);
    }

    @Test
    void testGetOrderWithDetails_Success() throws Exception {
        when(orderDetailService.getOrderWithDetailsById(1L)).thenReturn(orderDTO);

        OrderDTO result = orderManagementFacade.getOrderWithDetails(1L);

        assertNotNull(result);
        verify(orderDetailService, times(1)).getOrderWithDetailsById(1L);
    }

    @Test
    void testGetOrderBySessionId_Success() throws Exception {
        when(orderService.findByStripeSessionId("sessionId")).thenReturn(orderDTO);

        OrderDTO result = orderManagementFacade.getOrderBySessionId("sessionId");

        assertNotNull(result);
        verify(orderService, times(1)).findByStripeSessionId("sessionId");
    }

    @Test
    void testGetOrderBySessionId_OrderNotFound() {
        when(orderService.findByStripeSessionId("sessionId")).thenReturn(null);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            orderManagementFacade.getOrderBySessionId("sessionId");
        });

        assertEquals("Commande non trouvée pour la session : sessionId", exception.getMessage());
        verify(orderService, times(1)).findByStripeSessionId("sessionId");
    }

    @Test
    void testSendTicketsForOrder() {
        Order order = new Order();
        order.setId(1L);
        when(orderMapper.toEntity(orderDTO)).thenReturn(order);

        orderManagementFacade.sendTicketsForOrder(orderDTO);

        verify(emailService, times(1)).sendTicket(order);
    }
}
