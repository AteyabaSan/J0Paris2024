package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.joparis2024.model.Role;
import com.joparis2024.model.User;
import com.joparis2024.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Optional;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Crée un utilisateur avec un rôle
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRoles(Arrays.asList(new Role(1L, "ROLE_USER")));  // Ajoute un rôle pour éviter les erreurs

        // Mock le repository pour renvoyer cet utilisateur
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Appel de la méthode
        UserDetails result = customUserDetailsService.loadUserByUsername("testuser");

        // Vérifications
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }


    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("unknown");
        });
    }
}
