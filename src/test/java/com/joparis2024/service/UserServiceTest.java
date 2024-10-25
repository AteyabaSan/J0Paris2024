package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.joparis2024.dto.UserDTO;
import com.joparis2024.mapper.UserMapper;
import com.joparis2024.model.User;
import com.joparis2024.repository.RoleRepository;
import com.joparis2024.repository.UserRepository;

import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        // Any setup if necessary, but usually @InjectMocks does this already
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDTOs(users)).thenReturn(new ArrayList<>());

        // Act
        List<UserDTO> userDTOs = userService.getAllUsers();

        // Assert
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDTOs(users);
        assertNotNull(userDTOs);
    }

    @Test
    void testCreateUser_UserExists() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("testuser");  // Ajoutez un nom d'utilisateur valide
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> userService.createUser(userDTO));
        assertEquals("Email déjà utilisé", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail(userDTO.getEmail());
    }


    @Test
    void testCreateUser_Success() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("testuser");
        User user = new User();
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userMapper.toEntity(userDTO, null)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.createUser(userDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).existsByEmail(userDTO.getEmail());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    void testGetUserByEmail_UserFound() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(new UserDTO());

        // Act
        Optional<UserDTO> result = userService.getUserByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    void testUpdateUserByEmail_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        UserDTO userDTO = new UserDTO();
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> userService.updateUserByEmail(email, userDTO));
        assertEquals("Utilisateur non trouvé avec cet email", exception.getMessage());
    }

    @Test
    void testDeleteUserByEmail_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> userService.deleteUserByEmail(email));
        assertEquals("Utilisateur non trouvé avec cet email", exception.getMessage());
    }

    @Test
    void testDeleteUserByEmail_UserFound() throws Exception {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUserByEmail(email);

        // Assert
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testAuthenticate_Success() {
        // Arrange
        String username = "testuser";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");
        user.setEnabled(true);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(userMapper.toDTO(user)).thenReturn(new UserDTO());

        // Act
        UserDTO result = userService.authenticate(username, password);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, user.getPassword());
    }

    @Test
    void testAuthenticate_WrongPassword() {
        // Arrange
        String username = "testuser";
        String password = "wrongpassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.authenticate(username, password));
        assertEquals("Erreur lors de l'authentification de l'utilisateur.", exception.getMessage());
    }
}
