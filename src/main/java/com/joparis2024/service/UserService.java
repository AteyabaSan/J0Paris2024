package com.joparis2024.service;

import com.joparis2024.dto.UserDTO;
import com.joparis2024.model.Role;
import com.joparis2024.model.User;
import com.joparis2024.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

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
    
    @PersistenceContext
    private EntityManager entityManager;

    // Récupérer tous les utilisateurs
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(mapToDTO(user));
        }
        return userDTOs;
    }

 // Créer un utilisateur à partir d'un UserDTO complet (scénario normal)
    @Transactional
    public UserDTO createUser(UserDTO userDTO) throws Exception {
        System.out.println("Tentative de création d'un utilisateur : " + userDTO.getEmail());

        // Valider les données du DTO utilisateur
        validateUserDTO(userDTO);

        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            System.out.println("Email déjà utilisé : " + userDTO.getEmail());
            throw new Exception("Email déjà utilisé");
        }

        // Créer un nouvel utilisateur
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(userDTO.getEnabled());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword()); // Assigne le mot de passe

        // Récupérer les rôles par leurs noms depuis la base de données
        List<Role> roles = roleRepository.findByNameIn(userDTO.getRoles());
        if (roles.isEmpty()) {
            throw new Exception("Les rôles spécifiés n'existent pas");
        }

        // Réattacher les rôles à la session Hibernate active
        List<Role> attachedRoles = new ArrayList<>();
        for (Role role : roles) {
            Role attachedRole = entityManager.merge(role);  // Merge pour attacher chaque rôle
            attachedRoles.add(attachedRole);
        }
        user.setRoles(attachedRoles);

        // Sauvegarde dans la base de données
        try {
            User savedUser = userRepository.save(user); 
            return mapToDTO(savedUser);
        } catch (Exception e) {
            e.printStackTrace();  // Affiche l'erreur complète dans les logs
            throw new Exception("Erreur lors de la création de l'utilisateur : " + e.getMessage());
        }
    }

    // Récupérer un utilisateur par email
    public Optional<UserDTO> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(this::mapToDTO);
    }

 // Mettre à jour un utilisateur par son email (UPDATE)
    public UserDTO updateUserByEmail(String email, UserDTO userDTO) throws Exception {
        Optional<User> existingUserOptional = userRepository.findByEmail(email);
        if (!existingUserOptional.isPresent()) {
            throw new Exception("Utilisateur non trouvé avec cet email");
        }

        User existingUser = existingUserOptional.get();
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setEnabled(userDTO.getEnabled());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());

        // Gérer les rôles seulement s'ils sont présents dans le DTO
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            List<Role> roles = roleRepository.findByNameIn(userDTO.getRoles());
            if (roles.isEmpty()) {
                throw new Exception("Les rôles spécifiés n'existent pas");
            }
            existingUser.setRoles(roles);
        }

        // Sauvegarder les modifications
        User updatedUser = userRepository.save(existingUser);
        return mapToDTO(updatedUser);
    }


    	// Supprimer un utilisateur (DELETE)
    @Transactional
    public void deleteUserByEmail(String email) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new Exception("Utilisateur non trouvé avec cet email");
        }
        userRepository.delete(user.get());
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
        if (user == null) {
            throw new IllegalArgumentException("L'utilisateur à mapper est nul.");
        }

        List<String> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getName());
        }

        // Log pour vérifier le contenu de UserDTO après mapping
        UserDTO userDTO = new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            roles, // Mapping des rôles
            user.getEnabled(),
            user.getPhoneNumber(),
            user.getPassword()
        );
        
        System.out.println("Utilisateur mappé en UserDTO avec succès: " + userDTO);
        return userDTO;
    }


    public User mapToEntity(UserDTO userDTO) throws Exception {
        if (userDTO == null) {
            throw new IllegalArgumentException("Le UserDTO à mapper est nul.");
        }

        // Vérification de l'ID de l'utilisateur
        if (userDTO.getId() == null) {
            throw new Exception("L'ID de l'utilisateur dans UserDTO est manquant.");
        }

        // Création de l'entité User
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(userDTO.getEnabled());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        // Vérification des rôles dans UserDTO
        if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
            throw new Exception("Les rôles de l'utilisateur sont manquants ou incomplets.");
        }

        // Récupération des rôles par leurs noms depuis la base de données
        List<Role> roles = roleRepository.findByNameIn(userDTO.getRoles());
        if (roles.isEmpty()) {
            throw new Exception("Aucun rôle correspondant trouvé dans la base de données.");
        }

        // Log pour vérifier les rôles avant d'ajouter à l'utilisateur
        System.out.println("Rôles trouvés pour l'utilisateur: " + roles);

        user.setRoles(roles);

        // Log pour vérifier le contenu de User après mapping
        System.out.println("Utilisateur mappé avec succès: " + user);
        return user;
    }


    public List<UserDTO> mapToDTOs(List<User> users) {
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(mapToDTO(user));
        }

        // Log pour vérifier la conversion de la liste
        System.out.println("Conversion de la liste d'utilisateurs en UserDTOs terminée: " + userDTOs);
        return userDTOs;
    }


    public List<User> mapToEntities(List<UserDTO> userDTOs) throws Exception {
        List<User> users = new ArrayList<>();
        for (UserDTO userDTO : userDTOs) {
            users.add(mapToEntity(userDTO));
        }

        // Log pour vérifier la conversion de la liste
        System.out.println("Conversion de la liste de UserDTOs en entités User terminée: " + users);
        return users;
    }

    
 // Rechercher un utilisateur par nom d'utilisateur (username)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username); // Retourne null si non trouvé
    }
    
    public User findById(Long id) throws Exception {
        return userRepository.findById(id).orElseThrow(() -> new Exception("Utilisateur introuvable"));
    }




}



