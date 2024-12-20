package com.joparis2024.service;

import com.joparis2024.dto.UserDTO;
import com.joparis2024.mapper.UserMapper;
import com.joparis2024.model.Role;
import com.joparis2024.model.User;
import com.joparis2024.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import com.joparis2024.repository.RoleRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private UserMapper userMapper;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    // Récupérer tous les utilisateurs
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Utilisation d'une boucle pour initialiser les rôles
        for (User user : users) {
            Hibernate.initialize(user.getRoles());
        }
        return userMapper.toDTOs(users);
    }

    // Créer un utilisateur à partir d'un UserDTO complet
    @Transactional
    public UserDTO createUser(UserDTO userDTO) throws Exception {
        validateUserDTO(userDTO);

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new Exception("Email déjà utilisé");
        }

        // Créer un nouvel utilisateur sans rôle
        User user = userMapper.toEntity(userDTO, null);
        
        // Assurez-vous que l'attribut enabled est défini par défaut
        user.setEnabled(true); // Ou false selon votre logique métier

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    // Récupérer un utilisateur par email
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    Hibernate.initialize(user.getRoles()); // Initialiser les rôles
                    return userMapper.toDTO(user);
                });
    }

    // Mettre à jour un utilisateur par son email
    @Transactional
    public UserDTO updateUserByEmail(String email, UserDTO userDTO) throws Exception {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Utilisateur non trouvé avec cet email"));

        // Initialiser les rôles de l'utilisateur existant
        Hibernate.initialize(existingUser.getRoles());

        // Mettre à jour les détails de l'utilisateur
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setEnabled(userDTO.getEnabled());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());

        // Mettre à jour les rôles s'ils sont présents dans le DTO
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            List<Role> roles = roleRepository.findByNameIn(userDTO.getRoles());
            existingUser.setRoles(roles);
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    // Supprimer un utilisateur par email
    @Transactional
    public void deleteUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Utilisateur non trouvé avec cet email"));
        userRepository.delete(user);
    }

    // Méthodes auxiliaires de validation
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

    // Autres méthodes de service
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé pour le nom d'utilisateur : " + username));
    }


    @Transactional(readOnly = true)
    public User findById(Long id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Utilisateur introuvable"));
        Hibernate.initialize(user.getRoles());
        return user;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public UserDTO save(UserDTO userDTO) {
        try {
            // Vérifier si l'email existe déjà
            Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
            if (existingUser.isPresent()) {
                throw new RuntimeException("Un utilisateur avec cet email existe déjà.");
            }

            // Vérifier si le nom d'utilisateur existe déjà
            Optional<User> existingUsername = userRepository.findByUsername(userDTO.getUsername());
            if (existingUsername.isPresent()) {
                throw new RuntimeException("Un utilisateur avec ce nom d'utilisateur existe déjà.");
            }

            // Encodage du mot de passe
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            // Convertir le DTO en entité
            User user = userMapper.toEntity(userDTO);

            // Charger les rôles depuis la base de données
            List<Role> roles = new ArrayList<>();
            for (String roleName : userDTO.getRoles()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Rôle introuvable : " + roleName));
                roles.add(role);
            }

            // Assigner les rôles à l'utilisateur
            user.setRoles(roles);

            // Activer l'utilisateur
            user.setEnabled(true);

            // Sauvegarder l'utilisateur dans la base de données
            User savedUser = userRepository.save(user);

            // Retourner le DTO correspondant à l'utilisateur sauvegardé
            return userMapper.toDTO(savedUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'enregistrement de l'utilisateur", e);
        }
    }


    @Transactional
    public UserDTO authenticate(String username, String password) {
        try {
            // Vérifier si l'utilisateur avec ce nom existe
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Nom d'utilisateur incorrect."));

            // Vérifier le mot de passe
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Mot de passe incorrect.");
            }

            // Vérifier si l'utilisateur est activé
            if (!user.isEnabled()) {
                throw new RuntimeException("L'utilisateur n'est pas activé.");
            }

            // Retourner le DTO de l'utilisateur authentifié
            return userMapper.toDTO(user);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'authentification de l'utilisateur.", e);
        }
    }


}