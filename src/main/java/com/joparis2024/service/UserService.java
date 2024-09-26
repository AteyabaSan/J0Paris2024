package com.joparis2024.service;

import com.joparis2024.dto.UserDTO;
import com.joparis2024.model.Role;
import com.joparis2024.model.User;
import com.joparis2024.repository.UserRepository;
import com.joparis2024.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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
        System.out.println("Tentative de création d'un utilisateur : " + userDTO.getEmail());

        validateUserDTO(userDTO);

        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            System.out.println("Email déjà utilisé : " + userDTO.getEmail());
            throw new Exception("Email déjà utilisé");
        }

        // Création de l'objet User
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(userDTO.getEnabled());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        	// Gérer le mot de passe
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new Exception("Le mot de passe est obligatoire");
        }
        user.setPassword(userDTO.getPassword()); // Assigne le mot de passe

        // Gérer les rôles - récupérer les rôles par leurs noms depuis la base de données
        List<Role> roles = roleRepository.findByNameIn(userDTO.getRoles());
        if (roles.isEmpty()) {
            throw new Exception("Les rôles spécifiés n'existent pas");
        }
        user.setRoles(roles);

        // Sauvegarde dans la base de données
        User savedUser = userRepository.save(user);
        System.out.println("Utilisateur sauvegardé avec succès : " + savedUser.getId());

        return mapToDTO(savedUser);
    }

    // Récupérer un utilisateur par email
    public Optional<UserDTO> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(this::mapToDTO);
    }

    // Mettre à jour un utilisateur (UPDATE)
    public UserDTO updateUser(Long id, UserDTO userDTO) throws Exception {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (!existingUserOptional.isPresent()) {
            throw new Exception("Utilisateur non trouvé");
        }

        User existingUser = existingUserOptional.get();
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setEnabled(userDTO.getEnabled());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());

        // Gérer les rôles
        List<Role> roles = roleRepository.findByNameIn(userDTO.getRoles());
        if (roles.isEmpty()) {
            throw new Exception("Les rôles spécifiés n'existent pas");
        }
        existingUser.setRoles(roles);

        // Sauvegarde des modifications
        User updatedUser = userRepository.save(existingUser);
        return mapToDTO(updatedUser);
    }

    	// Supprimer un utilisateur (DELETE)
    	public void deleteUser(Long id) throws Exception {
    		if (!userRepository.existsById(id)) {
            throw new Exception("Utilisateur non trouvé");
    		}
    		userRepository.deleteById(id);
    }

    	// Vérifier si l'email existe déjà
    	public boolean emailExists(String email) {
    		return userRepository.existsByEmail(email);
    }

    // Méthodes auxiliaires
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
        List<String> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getName());
        }
        return new UserDTO(
        	user.getId(),
            user.getUsername(),
            user.getEmail(),
            roles, // Mapping des rôles
            user.getEnabled(),
            user.getPhoneNumber(),
            user.getPassword()
        );
    }

    public User mapToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(userDTO.getEnabled());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        // Gérer les rôles - récupérer les rôles par leurs noms depuis la base de données
        List<Role> roles = roleRepository.findByNameIn(userDTO.getRoles());
        user.setRoles(roles);

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



