package com.joparis2024.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.joparis2024.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("integration")  // Indique que ce test utilise le profil "integration"
@Transactional  // Utilise les transactions pour que les données soient effacées après chaque test
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        // Given
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("securePassword");
        user.setEmail("test@example.com");
        user.setRole("USER");
        user.setEnabled(true);
        userRepository.save(user); // Sauvegarde de l'utilisateur

        // When
        User foundUser = userRepository.findByUsername("testUser");

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testUser");
    }

    @Test
    void testFindByEmail() {
        // Given
        User user = new User();
        user.setUsername("anotherUser");
        user.setPassword("anotherPassword");
        user.setEmail("another@example.com");
        user.setRole("ADMIN");
        user.setEnabled(false);
        userRepository.save(user); // Sauvegarde de l'utilisateur

        // When
        User foundUser = userRepository.findByEmail("another@example.com");

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("another@example.com");
    }
}
