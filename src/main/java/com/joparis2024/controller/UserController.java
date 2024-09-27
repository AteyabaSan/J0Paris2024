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

    // Récupérer tous les utilisateurs (READ)
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Créer un utilisateur avec une liste de rôles (CREATE)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        try {
            System.out.println("Tentative de création d'un utilisateur : " + userDTO.getEmail());
            UserDTO createdUser = userService.createUser(userDTO);
            
            if (createdUser == null) {
                System.out.println("Création de l'utilisateur échouée : aucune donnée retournée.");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            System.out.println("Utilisateur créé avec succès : " + createdUser.getId());
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Récupérer un utilisateur par email (READ)
    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        Optional<UserDTO> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Vérifier si un email existe (READ)
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> emailExists(@PathVariable String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

 // Mettre à jour un utilisateur avec son email (UPDATE)
    @PutMapping("/email/{email}")
    public ResponseEntity<UserDTO> updateUserByEmail(@PathVariable String email, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updateUserByEmail = userService.updateUserByEmail(email, userDTO);
            return new ResponseEntity<>(updateUserByEmail, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

 // Supprimer un utilisateur par email
    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        try {
            userService.deleteUserByEmail(email);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}



