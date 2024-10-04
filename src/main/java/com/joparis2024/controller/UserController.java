package com.joparis2024.controller;

import com.joparis2024.dto.UserDTO;
import com.joparis2024.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // Récupérer tous les utilisateurs (READ)
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        logger.info("Récupération de tous les utilisateurs. Nombre d'utilisateurs : {}", users.size());
        return ResponseEntity.ok(users);
    }

    // Créer un utilisateur avec une liste de rôles (CREATE)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        try {
            logger.info("Tentative de création d'un utilisateur avec email : {}", userDTO.getEmail());
            UserDTO createdUser = userService.createUser(userDTO);
            
            if (createdUser == null) {
                logger.error("Création de l'utilisateur échouée : aucune donnée retournée.");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            logger.info("Utilisateur créé avec succès : {}", createdUser.getId());
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'utilisateur : ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Récupérer un utilisateur par email (READ)
    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        logger.info("Recherche de l'utilisateur avec email : {}", email);
        Optional<UserDTO> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> {
            logger.warn("Utilisateur non trouvé avec l'email : {}", email);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }

    // Vérifier si un email existe (READ)
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> emailExists(@PathVariable String email) {
        boolean exists = userService.emailExists(email);
        logger.info("Vérification de l'existence de l'email : {}. Existe : {}", email, exists);
        return ResponseEntity.ok(exists);
    }

    // Mettre à jour un utilisateur avec son email (UPDATE)
    @PutMapping("/email/{email}")
    public ResponseEntity<UserDTO> updateUserByEmail(@PathVariable String email, @RequestBody UserDTO userDTO) {
        try {
            logger.info("Tentative de mise à jour de l'utilisateur avec l'email : {}", email);
            UserDTO updatedUser = userService.updateUserByEmail(email, userDTO);
            logger.info("Utilisateur mis à jour avec succès pour l'email : {}", email);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'utilisateur avec l'email : {}", email, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Supprimer un utilisateur par email (DELETE)
    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        try {
            logger.info("Tentative de suppression de l'utilisateur avec l'email : {}", email);
            userService.deleteUserByEmail(email);
            logger.info("Utilisateur supprimé avec succès : {}", email);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'utilisateur avec l'email : {}", email, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
