package com.joparis2024.controller;

import com.joparis2024.dto.UserDTO;
import com.joparis2024.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Récupérer tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Créer un utilisateur
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        try {
            System.out.println("Tentative de création d'un utilisateur : " + userDTO.getEmail()); // Log pour vérifier les données reçues
            UserDTO createdUser = userService.createUser(userDTO);
            
            if (createdUser == null) {
                System.out.println("Création de l'utilisateur échouée : aucune donnée retournée."); // Log pour voir si la création échoue
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            System.out.println("Utilisateur créé avec succès : " + createdUser.getId()); // Log pour confirmer la création
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Log de l'erreur pour la traçabilité
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Récupérer un utilisateur par email
    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        Optional<UserDTO> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Vérifier si un email existe
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> emailExists(@PathVariable String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }
}

