package com.joparis2024.service;

import com.joparis2024.dto.UserDTO;
import com.joparis2024.model.User;
import com.joparis2024.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Récupérer tous les utilisateurs
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(mapToDTO(user));
        }
        return userDTOs;
    }

    // Créer un utilisateur
    public UserDTO createUser(UserDTO userDTO) throws Exception {
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

        // Sauvegarde et conversion en DTO
        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
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
    public UserDTO mapToDTO(User user) {
        return new UserDTO(
        	user.getId(),  // Ajout de l'ID
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.getEnabled(),
            user.getPhoneNumber()
        );
    }

    
    public User mapToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());  // Ajout de l'ID
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setEnabled(userDTO.getEnabled());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        return user;
    }
    
 // Convertir une liste de Users en une liste de UserDTOs
    public List<UserDTO> mapToDTOs(List<User> users) {
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(mapToDTO(user));
        }
        return userDTOs;
    }

    // Convertir une liste de UserDTOs en une liste de Users
    public List<User> mapToEntities(List<UserDTO> userDTOs) {
        List<User> users = new ArrayList<>();
        for (UserDTO userDTO : userDTOs) {
            users.add(mapToEntity(userDTO));
        }
        return users;
    }
}

