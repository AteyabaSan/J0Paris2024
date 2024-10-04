package com.joparis2024.service;

import com.joparis2024.dto.UserRoleDTO;
import com.joparis2024.mapper.UserRoleMapper;
import com.joparis2024.model.UserRole;
import com.joparis2024.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRoleMapper userRoleMapper; // Le mapper pour convertir entre DTO et entités

    // Créer une relation User_Role
    public UserRoleDTO createUserRole(UserRoleDTO userRoleDTO) throws Exception {
        UserRole userRole = userRoleMapper.toEntity(userRoleDTO); // Utiliser le mapper
        UserRole savedUserRole = userRoleRepository.save(userRole);
        return userRoleMapper.toDTO(savedUserRole);
    }

    // Mettre à jour une relation User_Role
    public UserRoleDTO updateUserRole(Long id, UserRoleDTO userRoleDTO) throws Exception {
        Optional<UserRole> userRoleOpt = userRoleRepository.findById(id);
        if (!userRoleOpt.isPresent()) {
            throw new Exception("Relation User_Role non trouvée");
        }

        UserRole userRole = userRoleOpt.get();
        userRole.setUser(userRoleDTO.getUser()); // Mettre à jour l'utilisateur
        userRole.setRole(userRoleDTO.getRole()); // Mettre à jour le rôle
        UserRole updatedUserRole = userRoleRepository.save(userRole);

        return userRoleMapper.toDTO(updatedUserRole);
    }

    // Supprimer une relation User_Role
    public void deleteUserRole(Long id) throws Exception {
        Optional<UserRole> userRoleOpt = userRoleRepository.findById(id);
        if (!userRoleOpt.isPresent()) {
            throw new Exception("Relation User_Role non trouvée");
        }
        userRoleRepository.delete(userRoleOpt.get());
    }

    // Récupérer toutes les relations User_Role
    public List<UserRoleDTO> getAllUserRoles() {
        List<UserRole> userRoles = userRoleRepository.findAll();
        List<UserRoleDTO> userRoleDTOs = new ArrayList<>();
        // Utilisation d'une boucle classique pour convertir chaque entité UserRole en DTO
        for (UserRole userRole : userRoles) {
            userRoleDTOs.add(userRoleMapper.toDTO(userRole));
        }
        return userRoleDTOs;
    }
}

