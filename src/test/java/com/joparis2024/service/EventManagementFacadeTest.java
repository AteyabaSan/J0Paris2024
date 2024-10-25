package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.TicketMapper;
import com.joparis2024.model.Event;
import com.joparis2024.model.EventOffer;
import com.joparis2024.model.Offer;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.EventOfferRepository;
import com.joparis2024.repository.EventRepository;
import com.joparis2024.repository.TicketRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class EventManagementFacadeTest {

    @Mock
    private EventService eventService;

    @Mock
    private OfferService offerService;

    @Mock
    private TicketService ticketService;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private EventOfferRepository eventOfferRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private EventManagementFacade eventManagementFacade;

    private Event event;
    private EventDTO eventDTO;
    private Ticket ticket;
    private TicketDTO ticketDTO;
    private Offer offer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        event = new Event();
        event.setId(1L);
        event.setEventName("Test Event");

        eventDTO = new EventDTO();
        eventDTO.setId(1L);
        eventDTO.setEventName("Test Event");

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setPrice(50.0);

        ticketDTO = new TicketDTO();
        ticketDTO.setId(1L);
        ticketDTO.setPrice(50.0);

        offer = new Offer();
        offer.setId(1L);
        offer.setName("Test Offer");
    }

    @Test
    void testAssignTicketsToEvent_Success() throws Exception {
        List<Long> ticketIds = Arrays.asList(1L);
        when(eventService.getEventById(1L)).thenReturn(eventDTO);
        when(ticketService.getTicketById(1L)).thenReturn(ticketDTO);
        when(ticketMapper.toEntity(ticketDTO)).thenReturn(ticket);
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        // Appel du service
        eventManagementFacade.assignTicketsToEvent(1L, ticketIds);

        // Vérification des appels
        verify(eventService, times(1)).getEventById(1L);
        verify(ticketService, times(1)).getTicketById(1L);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void testGetTicketsForEvent_Success() throws Exception {
        List<Ticket> tickets = Arrays.asList(ticket);
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(ticketRepository.findTicketsWithEvent(1L)).thenReturn(tickets);

        // Appel du service
        List<Ticket> result = eventManagementFacade.getTicketsForEvent(1L);

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(50.0, result.get(0).getPrice());

        verify(ticketRepository, times(1)).findTicketsWithEvent(1L);
    }

    @Test
    void testAssignOffersToEvent_Success() throws Exception {
        List<Long> offerIds = Arrays.asList(1L);
        
        // Mock de la récupération de l'EventDTO
        when(eventService.getEventById(1L)).thenReturn(eventDTO);
        
        // Mock de la conversion en Event
        when(eventService.toEntity(eventDTO)).thenReturn(event);

        // Mock de la récupération de l'Offer
        when(offerService.findById(1L)).thenReturn(offer);
        
        // Mock de la vérification de l'existence de l'association Event-Offer
        when(eventOfferRepository.findByEventIdAndOfferId(1L, 1L)).thenReturn(Optional.empty());

        // Appel du service
        eventManagementFacade.assignOffersToEvent(1L, offerIds);

        // Vérification des appels
        verify(eventService, times(1)).getEventById(1L);
        verify(eventService, times(1)).toEntity(eventDTO);
        verify(offerService, times(1)).findById(1L);
        verify(eventOfferRepository, times(1)).save(any());
    }

    @Test
    void testAssignOffersToEvent_OfferAlreadyAssigned() throws Exception {
        List<Long> offerIds = Arrays.asList(1L);
        
        // Mock de la récupération de l'EventDTO
        when(eventService.getEventById(1L)).thenReturn(eventDTO);
        
        // Mock de la conversion en Event
        when(eventService.toEntity(eventDTO)).thenReturn(event);

        // Mock de la récupération de l'Offer
        when(offerService.findById(1L)).thenReturn(offer);
        
        // Mock d'une relation déjà existante
        when(eventOfferRepository.findByEventIdAndOfferId(1L, 1L)).thenReturn(Optional.of(new EventOffer()));

        // Appel du service
        eventManagementFacade.assignOffersToEvent(1L, offerIds);

        // Vérification que l'assignation n'a pas été ré-exécutée
        verify(eventOfferRepository, never()).save(any());
    }


    @Test
    void testGetOffersForEvent_Success() throws Exception {
        when(eventService.getEventById(1L)).thenReturn(eventDTO);
        when(eventOfferRepository.findByEvent(any())).thenReturn(Arrays.asList(new EventOffer()));

        // Appel du service
        List<Offer> offers = eventManagementFacade.getOffersForEvent(1L);

        // Vérifications
        assertNotNull(offers);
        verify(eventOfferRepository, times(1)).findByEvent(any());
    }
    
    @Test
    void testDeleteEventOffer_Success() throws Exception {
        when(eventOfferRepository.findByEventIdAndOfferId(1L, 1L)).thenReturn(Optional.of(new EventOffer()));

        // Appel du service
        eventManagementFacade.deleteEventOffer(1L, 1L);

        // Vérification des appels
        verify(eventOfferRepository, times(1)).delete(any(EventOffer.class));
    }

    @Test
    void testGetEventWithTicketDetails_Success() throws Exception {
        List<Ticket> tickets = Arrays.asList(ticket);
        when(eventService.getEventById(1L)).thenReturn(eventDTO);
        when(ticketRepository.findByEventId(1L)).thenReturn(tickets);
        when(ticketMapper.toDTOs(tickets)).thenReturn(Arrays.asList(ticketDTO));

        // Appel du service
        EventDTO result = eventManagementFacade.getEventWithTicketDetails(1L);

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1, result.getTickets().size());

        verify(ticketRepository, times(1)).findByEventId(1L);
    }
}
