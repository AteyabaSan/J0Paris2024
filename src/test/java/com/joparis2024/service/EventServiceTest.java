package com.joparis2024.service;

//import com.joparis2024.repository.EventRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;

public class EventServiceTest {

//    @Mock
//    private EventRepository eventRepository;
//
//    @Mock
//    private TicketService ticketService;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private EventService eventService;
//
//    private EventDTO eventDTO;
//    private Event event;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Initialisation d'un EventDTO pour les tests
//        eventDTO = new EventDTO();
//        eventDTO.setEventName("100 mètres");
//        eventDTO.setDate(null);
//        eventDTO.setLocation("Stade Olympique");
//        eventDTO.setCategory("Athlétisme");
//        eventDTO.setPriceRange(120.0);
//        eventDTO.setAvailableTickets(500);
//        eventDTO.setDescription("Finale du 100 mètres");
//        eventDTO.setSoldOut(false);
//
//        // Initialisation d'un Event pour les tests
//        event = new Event();
//        event.setEventName("100 mètres");
//        event.setDate(null);
//        event.setLocation("Stade Olympique");
//        event.setCategory("Athlétisme");
//        event.setPriceRange(120.0);
//        event.setAvailableTickets(500);
//        event.setDescription("Finale du 100 mètres");
//        event.setSoldOut(false);
//    }
//
//    // Cas où la création d'un événement fonctionne
//    @Test
//    public void createEvent_Success() {
//        // Arrange : Préparation des données de test
//        // On s'assure que le repository renvoie un Event correct quand save() est appelé
//        when(eventRepository.save(any(Event.class))).thenReturn(event);
//
//        // Act : Appel de la méthode à tester
//        Event createdEvent = eventService.createEvent(eventDTO);
//
//        // Assert : Vérification que l'événement créé n'est pas null et que les valeurs sont correctes
//        assertNotNull(createdEvent, "L'événement créé ne doit pas être null");  // Vérifie que l'objet n'est pas null
//        assertEquals(eventDTO.getEventName(), createdEvent.getEventName(), "Le nom de l'événement doit correspondre");  // Vérifie que les données sont correctes
//        
//        // Vérification que la méthode save() a bien été appelée une fois avec n'importe quel objet Event
//        verify(eventRepository, times(1)).save(any(Event.class));
//    }
//
//
//    // Cas où un événement est récupéré par son nom
//    @Test
//    public void getEventByName_Success() {
//        when(eventRepository.findByEventName(anyString())).thenReturn(Optional.of(event));
//
//        Optional<EventDTO> foundEvent = eventService.getEventByName("100 mètres");
//
//        assertTrue(foundEvent.isPresent());
//        assertEquals(eventDTO.getEventName(), foundEvent.get().getEventName());
//        verify(eventRepository).findByEventName(anyString());
//    }
//
//    // Cas où la suppression d'un événement fonctionne
//    @Test
//    public void deleteEvent_Success() throws Exception {
//        when(eventRepository.findByEventName(anyString())).thenReturn(Optional.of(event));
//
//        eventService.deleteEvent("100 mètres");
//
//        verify(eventRepository).delete(any(Event.class));
//    }
//
//    // Cas où la mise à jour d'un événement fonctionne
//    @Test
//    public void updateEvent_Success() throws Exception {
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//        when(eventRepository.save(any(Event.class))).thenReturn(event);
//
//        Event updatedEvent = eventService.updateEvent(1L, eventDTO);
//
//        assertNotNull(updatedEvent);
//        assertEquals(eventDTO.getEventName(), updatedEvent.getEventName());
//        verify(eventRepository).save(any(Event.class));
//    }
//
//    // Cas où un événement n'est pas trouvé
//    @Test
//    public void getEventByName_NotFound() {
//        when(eventRepository.findByEventName(anyString())).thenReturn(Optional.empty());
//
//        Optional<EventDTO> foundEvent = eventService.getEventByName("Marathon");
//
//        assertFalse(foundEvent.isPresent());
//        verify(eventRepository).findByEventName(anyString());
//    }
//
//    // Cas où la suppression d'un événement échoue
//    @Test
//    public void deleteEvent_NotFound() {
//        when(eventRepository.findByEventName(anyString())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            eventService.deleteEvent("Marathon");
//        });
//
//        assertEquals("Événement non trouvé", exception.getMessage());
//        verify(eventRepository, never()).delete(any(Event.class));
//    }
//
//    // Cas où les événements sont récupérés par catégorie
//    @Test
//    public void getEventsByCategory_Success() {
//        when(eventRepository.findByCategory(anyString())).thenReturn(Arrays.asList(event));
//
//        List<EventDTO> eventsByCategory = eventService.getEventsByCategory("Athlétisme");
//
//        assertFalse(eventsByCategory.isEmpty());
//        assertEquals(1, eventsByCategory.size());
//        assertEquals(eventDTO.getEventName(), eventsByCategory.get(0).getEventName());
//        verify(eventRepository).findByCategory(anyString());
//    }
}
