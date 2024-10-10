package com.joparis2024.mapper;

import com.joparis2024.dto.UserRoleDTO;
import com.joparis2024.model.UserRole;
import com.joparis2024.repository.RoleRepository;
import com.joparis2024.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRoleMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Mapper UserRole -> UserRoleDTO
    public UserRoleDTO toDTO(UserRole userRole) {
        if (userRole == null) {
            return null;
        }

        UserRoleDTO dto = new UserRoleDTO();
        dto.setId(userRole.getId());
        dto.setUserId(userRole.getUser().getId());  // Utilisation de l'ID de l'utilisateur
        dto.setRoleId(userRole.getRole().getId());  // Utilisation de l'ID du rôle

        return dto;
    }

    // Mapper UserRoleDTO -> UserRole
    public UserRole toEntity(UserRoleDTO userRoleDTO) throws Exception {
        if (userRoleDTO == null) {
            return null;
        }

        UserRole userRole = new UserRole();
        userRole.setId(userRoleDTO.getId());
        userRole.setUser(userRepository.findById(userRoleDTO.getUserId())
                        .orElseThrow(() -> new Exception("Utilisateur non trouvé")));  // Récupérer l'utilisateur par ID
        userRole.setRole(roleRepository.findById(userRoleDTO.getRoleId())
                        .orElseThrow(() -> new Exception("Rôle non trouvé")));  // Récupérer le rôle par ID

        return userRole;
    }


 // Mapper liste de UserRole -> liste de UserRoleDTO
    public List<UserRoleDTO> toDTOs(List<UserRole> userRoles) {
        List<UserRoleDTO> userRoleDTOs = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les entités en DTOs
        for (UserRole userRole : userRoles) {
            userRoleDTOs.add(toDTO(userRole));  // Appel à la méthode de conversion pour chaque entité
        }
        return userRoleDTOs;
    }

    // Mapper liste de UserRoleDTO -> liste de UserRole
    public List<UserRole> toEntities(List<UserRoleDTO> userRoleDTOs) throws Exception {
        List<UserRole> userRoles = new ArrayList<>();
        
        for (UserRoleDTO userRoleDTO : userRoleDTOs) {
            userRoles.add(toEntity(userRoleDTO));  // Conversion with propagation of the exception
        }

        return userRoles;
    }

}