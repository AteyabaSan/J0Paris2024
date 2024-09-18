package com.joparis2024.service;

import com.joparis2024.dto.UserDTO;
import com.joparis2024.model.User;
import com.joparis2024.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Récupérer tous les utilisateurs
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Créer un utilisateur
    public User createUser(UserDTO userDTO) throws Exception {
        validateUserDTO(userDTO);

        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new Exception("Email déjà utilisé");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setEnabled(userDTO.getEnabled());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        return userRepository.save(user);
    }

    // Récupérer un utilisateur par email
    public Optional<UserDTO> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(this::mapToDTO);
    }

    // Vérifier si l'email existe déjà
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    //Methodes auxiliaires
    private void validateUserDTO(UserDTO userDTO) throws Exception {
        if (userDTO.getEmail() == null || !isValidEmail(userDTO.getEmail())) {
            throw new Exception("Email non valide");
        }
        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
            throw new Exception("Le nom d'utilisateur ne peut pas être vide");
        }
    }

    private boolean isValidEmail(String email) {
        return email.contains("@");
    }

    // Mapper l'entité User vers un DTO UserDTO
    private UserDTO mapToDTO(User user) {
        return new UserDTO(user.getUsername(), user.getEmail(), user.getRole(), user.getEnabled(), user.getPhoneNumber());
    }
}

