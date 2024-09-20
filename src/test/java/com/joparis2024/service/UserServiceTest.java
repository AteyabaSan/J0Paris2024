package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.joparis2024.dto.UserDTO;
import com.joparis2024.model.User;
import com.joparis2024.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setEmail("testuser@example.com");
        userDTO.setRole("ROLE_USER");
        userDTO.setEnabled(true);
        userDTO.setPhoneNumber("1234567890");
    }

    // Test de création d'un utilisateur avec succès
    @Test
    public void createUser_Success() throws Exception {
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        verify(userRepository).save(any(User.class));
        verify(userRepository).existsByEmail(any(String.class));
    }

    // Test pour la création d'un utilisateur avec un email déjà existant
    @Test
    public void createUser_EmailExists() {
        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.createUser(userDTO);
        });

        String expectedMessage = "Email déjà utilisé";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(userRepository).existsByEmail(any(String.class));
    }

    // Test pour la récupération d'un utilisateur par email
    @Test
    public void getUserByEmail_Success() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        Optional<UserDTO> foundUser = userService.getUserByEmail("testuser@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
        verify(userRepository).findByEmail(any(String.class));
    }

    // Test pour la vérification de l'existence d'un email
    @Test
    public void emailExists_Success() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(new User()));

        boolean emailExists = userService.emailExists("testuser@example.com");

        assertTrue(emailExists);
        verify(userRepository).findByEmail(any(String.class));
    }
}
