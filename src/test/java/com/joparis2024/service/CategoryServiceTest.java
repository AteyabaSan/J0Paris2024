package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.joparis2024.dto.CategoryDTO;
import com.joparis2024.mapper.CategoryMapper;
import com.joparis2024.model.Category;
import com.joparis2024.model.Event;
import com.joparis2024.repository.CategoryRepository;
import com.joparis2024.repository.EventRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryDTO categoryDTO;
    private Event event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Sport");

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Sport");

        event = new Event();
        event.setId(1L);
        event.setEventName("Olympics 2024");
    }

    @Test
    void testCreateCategory_Success() throws Exception {
        when(categoryMapper.toEntity(categoryDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        // Appel du service
        CategoryDTO result = categoryService.createCategory(categoryDTO);

        // Vérifications
        assertNotNull(result);
        assertEquals("Sport", result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testGetAllCategories_Success() {
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        // Appel du service
        List<CategoryDTO> result = categoryService.getAllCategories();

        // Vérifications
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sport", result.get(0).getName());
    }

    @Test
    void testGetCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        // Appel du service
        Optional<CategoryDTO> result = categoryService.getCategoryById(1L);

        // Vérifications
        assertTrue(result.isPresent());
        assertEquals("Sport", result.get().getName());
    }

    @Test
    void testUpdateCategory_Success() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        // Appel du service
        CategoryDTO result = categoryService.updateCategory(1L, categoryDTO);

        // Vérifications
        assertNotNull(result);
        assertEquals("Sport", result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testDeleteCategory_Success() throws Exception {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        // Appel du service
        categoryService.deleteCategory(1L);

        // Vérifications
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAssignCategoryToEvents_Success() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));

        // Appel du service
        categoryService.assignCategoryToEvents(categoryDTO);

        // Vérifications
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testRemoveCategoryFromEvents_Success() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(eventRepository.findByCategory(category)).thenReturn(Arrays.asList(event));

        // Appel du service
        categoryService.removeCategoryFromEvents(1L);

        // Vérifications
        verify(eventRepository, times(1)).save(event);
        assertNull(event.getCategory());
    }
}
