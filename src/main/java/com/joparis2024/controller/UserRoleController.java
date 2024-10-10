package com.joparis2024.controller;

import com.joparis2024.dto.UserRoleDTO;  // Adapter le chemin selon ton projet
import com.joparis2024.service.UserRoleService;  // Adapter le chemin selon ton projet
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleController.class);

    @Autowired
    private UserRoleService userRoleService;

    // Créer une relation User_Role
    @PostMapping
    public ResponseEntity<UserRoleDTO> createUserRole(@RequestBody UserRoleDTO userRoleDTO) {
        try {
            logger.info("Requête pour créer une relation User_Role.");
            UserRoleDTO createdUserRole = userRoleService.createUserRole(userRoleDTO);
            logger.info("Relation User_Role créée avec succès : {}", createdUserRole);
            return new ResponseEntity<>(createdUserRole, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la relation User_Role : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Mettre à jour une relation User_Role
    @PutMapping("/{id}")
    public ResponseEntity<UserRoleDTO> updateUserRole(@PathVariable Long id, @RequestBody UserRoleDTO userRoleDTO) {
        try {
            logger.info("Requête pour mettre à jour la relation User_Role avec ID : {}", id);
            UserRoleDTO updatedUserRole = userRoleService.updateUserRole(id, userRoleDTO);
            logger.info("Relation User_Role mise à jour avec succès : {}", updatedUserRole);
            return new ResponseEntity<>(updatedUserRole, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la relation User_Role avec ID : {}", id, e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer une relation User_Role
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUserRole(@PathVariable Long id) {
        try {
            logger.info("Requête pour supprimer la relation User_Role avec ID : {}", id);
            userRoleService.deleteUserRole(id);
            logger.info("Relation User_Role avec ID : {} supprimée avec succès", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la relation User_Role avec ID : {}", id, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer toutes les relations User_Role
    @GetMapping
    public ResponseEntity<List<UserRoleDTO>> getAllUserRoles() {
        try {
            logger.info("Requête pour récupérer toutes les relations User_Role.");
            List<UserRoleDTO> userRoleDTOs = userRoleService.getAllUserRoles();
            logger.info("Nombre de relations User_Role récupérées : {}", userRoleDTOs.size());
            return new ResponseEntity<>(userRoleDTOs, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des relations User_Role : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer une relation User_Role par ID
    @GetMapping("/{id}")
    public ResponseEntity<UserRoleDTO> getUserRoleById(@PathVariable Long id) {
        try {
            logger.info("Requête pour récupérer la relation User_Role avec ID : {}", id);
            UserRoleDTO userRoleDTO = userRoleService.getUserRoleById(id);
            logger.info("Relation User_Role récupérée avec succès : {}", userRoleDTO);
            return new ResponseEntity<>(userRoleDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la relation User_Role avec ID : {}", id, e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
