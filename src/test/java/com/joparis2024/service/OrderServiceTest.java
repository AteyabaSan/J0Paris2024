package com.joparis2024.service;

//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
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
//import com.joparis2024.dto.OrderDTO;
//import com.joparis2024.dto.TicketDTO;
//import com.joparis2024.dto.UserDTO;
//import com.joparis2024.model.Order;
//import com.joparis2024.model.Ticket;
//import com.joparis2024.model.User;
//import com.joparis2024.repository.OrderRepository;
//
//@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private TicketService ticketService;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    private OrderDTO orderDTO;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        orderDTO = new OrderDTO();
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername("testUser");
//        orderDTO.setUser(userDTO);
//        TicketDTO ticketDTO = new TicketDTO();
//        ticketDTO.setPrice(50.0);
//        ticketDTO.setQuantity(2);
//        orderDTO.setTickets(Collections.singletonList(ticketDTO));
//        orderDTO.setTotalAmount(100.0);
//        orderDTO.setPaymentDate(LocalDateTime.now());
//    }
//
//    // Cas où la création de commande fonctionne
//    @Test
//    public void createOrder_Success() throws Exception {
//        // Simuler les valeurs de retour pour les services
//        when(userService.mapToEntity(any(UserDTO.class))).thenReturn(new User());
//        when(ticketService.mapToEntity(any(TicketDTO.class))).thenReturn(new Ticket());
//
//        // Simuler la sauvegarde et s'assurer que le montant total est bien défini dans l'ordre
//        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
//            Order order = invocation.getArgument(0);
//            order.setTotalAmount(orderDTO.getTotalAmount()); // S'assurer que le montant est défini
//            return order;
//        });
//
//        // Appel de la méthode à tester
//        Order createdOrder = orderService.createOrder(orderDTO);
//
//        // Vérifications des assertions
//        assertNotNull(createdOrder);
//        assertEquals(orderDTO.getTotalAmount(), createdOrder.getTotalAmount(), "Le montant total doit être égal à 100.0");
//
//        // Vérification que le repository et les services ont été appelés
//        verify(orderRepository).save(any(Order.class));
//        verify(userService).mapToEntity(any(UserDTO.class));
//        verify(ticketService).mapToEntity(any(TicketDTO.class));
//    }
//
//    // Cas où la création de commande échoue (exemple basique)
//    @Test
//    public void createOrder_Failure() throws Exception {
//        // Simuler un problème avec l'utilisateur (utilisateur non mappé)
//        when(userService.mapToEntity(any(UserDTO.class))).thenReturn(null);
//
//        // Assurer que l'ordre ne peut pas être créé et qu'une exception est levée
//        Exception exception = assertThrows(Exception.class, () -> {
//            orderService.createOrder(orderDTO);
//        });
//
//        // Vérifier le message de l'exception pour s'assurer qu'il correspond à celui dans la méthode createOrder
//        assertEquals("L'utilisateur est introuvable", exception.getMessage());
//
//        // Vérifier que les autres services ne sont pas appelés si l'utilisateur échoue
//        verify(orderRepository, org.mockito.Mockito.never()).save(any(Order.class));
//        verify(ticketService, org.mockito.Mockito.never()).mapToEntity(any(TicketDTO.class));
//    }
//    
//    //Test de mise à jour d'une commande 
//    
//    @Test
//    public void updateOrder_Success() throws Exception {
//        Order existingOrder = new Order();
//        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(existingOrder));
//        when(ticketService.mapToEntity(any(TicketDTO.class))).thenReturn(new Ticket());
//
//        OrderDTO orderDTO = new OrderDTO();
//        orderDTO.setTotalAmount(200.0);
//        orderDTO.setTickets(Collections.singletonList(new TicketDTO()));
//        
//     // Ajout de cette simulation avant l'appel à updateOrder
//        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
//            Order order = invocation.getArgument(0);
//            return order;  // Retourne l'objet commande mis à jour
//        });
//        
//        Order updatedOrder = orderService.updateOrder(1L, orderDTO);
//
//        assertEquals(orderDTO.getTotalAmount(), updatedOrder.getTotalAmount());
//        verify(orderRepository).save(any(Order.class));
//    }
//
//    @Test
//    public void updateOrder_Failure() {
//        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            orderService.updateOrder(1L, new OrderDTO());
//        });
//
//        assertEquals("Order non trouvée", exception.getMessage());
//        verify(orderRepository, org.mockito.Mockito.never()).save(any(Order.class));
//    }
//    
//    //Test de récupération de toutes les commandes
//    @Test
//    public void getAllOrders_Success() throws Exception {  // Ajoute 'throws Exception' si nécessaire
//        List<Order> orders = Collections.singletonList(new Order());
//        when(orderRepository.findAll()).thenReturn(orders);
//
//        List<OrderDTO> orderDTOs = orderService.getAllOrders();  // Vérifie que la méthode est bien appelée sans erreur
//
//        assertNotNull(orderDTOs);
//        assertFalse(orderDTOs.isEmpty());
//        verify(orderRepository).findAll();
//    }
//    
//    //Test d'annulation d'une commande
//    @Test
//    public void cancelOrder_Success() throws Exception {
//        Order existingOrder = new Order();
//        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(existingOrder));
//
//        orderService.cancelOrder(1L);
//
//        verify(orderRepository).delete(existingOrder);
//    }
//
//    @Test
//    public void cancelOrder_Failure() {
//        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            orderService.cancelOrder(1L);
//        });
//
//        String expectedMessage = "Erreur lors de l'annulation de la commande: Order non trouvée";
//        assertEquals(expectedMessage, exception.getMessage());
//    }


}


