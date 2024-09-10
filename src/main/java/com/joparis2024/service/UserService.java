package com.joparis2024.service;

import com.joparis2024.dto.UserDTO;
import com.joparis2024.model.User;
import com.joparis2024.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Récupérer tous les utilisateurs
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Créer un utilisateur
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setEnabled(userDTO.getEnabled());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode("defaultPassword"));  // Ajouter une méthode pour encoder les mots de passe
        return userRepository.save(user);
    }

    // Récupérer un utilisateur par ID
    public Optional<UserDTO> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(this::mapToDTO);
    }

    // Vérifier si l'email existe déjà
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    // Mapper l'entité User vers un DTO UserDTO
    private UserDTO mapToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getEnabled(), user.getPhoneNumber());
    }
}
