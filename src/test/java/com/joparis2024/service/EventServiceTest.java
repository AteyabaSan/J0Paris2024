package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.mapper.EventMapper;
import com.joparis2024.model.Category;
import com.joparis2024.model.Event;
import com.joparis2024.repository.CategoryRepository;
import com.joparis2024.repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void testGetAllEvents_Success() {
        List<Event> events = new ArrayList<>();
        events.add(new Event());
        when(eventRepository.findAll()).thenReturn(events);
        when(eventMapper.toDTOs(events)).thenReturn(new ArrayList<>());

        List<EventDTO> result = eventService.getAllEvents();

        assertNotNull(result);
        verify(eventRepository, times(1)).findAll();
        verify(eventMapper, times(1)).toDTOs(events);
    }

    @Test
    void testCreateEvent_Success() {
        // Arrange
        EventDTO eventDTO = new EventDTO();
        eventDTO.setCategoryId(1L);
        eventDTO.setEventName("Test Event");  // Nom d'événement valide
        Category category = new Category();
        Event event = new Event();
        when(categoryRepository.findById(eventDTO.getCategoryId())).thenReturn(Optional.of(category));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // Act
        EventDTO result = eventService.createEvent(eventDTO);

        // Assert
        assertNotNull(result);
        verify(eventRepository, times(1)).save(any(Event.class));
    }


    @Test
    void testCreateEvent_CategoryNotFound() {
        // Arrange
        EventDTO eventDTO = new EventDTO();
        eventDTO.setCategoryId(1L);
        eventDTO.setEventName("Test Event");  // Ajout du nom d'événement pour passer la validation
        when(categoryRepository.findById(eventDTO.getCategoryId())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(eventDTO));
        assertEquals("Catégorie non trouvée pour l'ID: " + eventDTO.getCategoryId(), exception.getMessage());
    }


    @Test
    void testUpdateEvent_Success() throws Exception {
        Long eventId = 1L;
        EventDTO eventDTO = new EventDTO();
        Event event = new Event();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toDTO(event)).thenReturn(eventDTO);

        EventDTO result = eventService.updateEvent(eventId, eventDTO);

        assertNotNull(result);
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(1)).save(event);
        verify(eventMapper, times(1)).toDTO(event);
    }

    @Test
    void testUpdateEvent_EventNotFound() {
        Long eventId = 1L;
        EventDTO eventDTO = new EventDTO();
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> eventService.updateEvent(eventId, eventDTO));
        assertEquals("Événement non trouvé", exception.getMessage());
    }

    @Test
    void testDeleteEvent_Success() throws Exception {
        Long eventId = 1L;
        Event event = new Event();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        eventService.deleteEvent(eventId);

        verify(eventRepository, times(1)).delete(event);
    }

    @Test
    void testDeleteEvent_EventNotFound() {
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> eventService.deleteEvent(eventId));
        assertEquals("Événement non trouvé", exception.getMessage());
    }

    @Test
    void testGetEventById_Success() throws Exception {
        Long eventId = 1L;
        Event event = new Event();
        EventDTO eventDTO = new EventDTO();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventMapper.toDTO(event)).thenReturn(eventDTO);

        EventDTO result = eventService.getEventById(eventId);

        assertNotNull(result);
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventMapper, times(1)).toDTO(event);
    }

    @Test
    void testGetEventById_EventNotFound() {
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> eventService.getEventById(eventId));
        assertEquals("Événement non trouvé", exception.getMessage());
    }

    @Test
    void testGetEventsByCategory_Success() {
        Long categoryId = 1L;
        List<Event> events = new ArrayList<>();
        events.add(new Event());
        when(eventRepository.findByCategoryId(categoryId)).thenReturn(events);
        when(eventMapper.toDTOs(events)).thenReturn(new ArrayList<>());

        List<EventDTO> result = eventService.getEventsByCategory(categoryId);

        assertNotNull(result);
        verify(eventRepository, times(1)).findByCategoryId(categoryId);
        verify(eventMapper, times(1)).toDTOs(events);
    }
}
