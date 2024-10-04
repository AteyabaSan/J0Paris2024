package com.joparis2024.mapper;

import com.joparis2024.dto.UserRoleDTO;
import com.joparis2024.model.UserRole;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRoleMapper {

    // Mapper UserRole -> UserRoleDTO
    public UserRoleDTO toDTO(UserRole userRole) {
        if (userRole == null) {
            return null;
        }

        UserRoleDTO dto = new UserRoleDTO();
        dto.setId(userRole.getId());
        dto.setUser(userRole.getUser());  // Associer directement l'utilisateur
        dto.setRole(userRole.getRole());  // Associer directement le rôle

        return dto;
    }

    // Mapper UserRoleDTO -> UserRole
    public UserRole toEntity(UserRoleDTO userRoleDTO) {
        if (userRoleDTO == null) {
            return null;
        }

        UserRole userRole = new UserRole();
        userRole.setId(userRoleDTO.getId());
        userRole.setUser(userRoleDTO.getUser());  // Associer directement l'utilisateur
        userRole.setRole(userRoleDTO.getRole());  // Associer directement le rôle

        return userRole;
    }

    // Mapper liste de UserRole -> liste de UserRoleDTO
    public List<UserRoleDTO> toDTOs(List<UserRole> userRoles) {
        List<UserRoleDTO> userRoleDTOs = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les entités en DTOs
        for (UserRole userRole : userRoles) {
            userRoleDTOs.add(toDTO(userRole));
        }

        return userRoleDTOs;
    }

    // Mapper liste de UserRoleDTO -> liste de UserRole
    public List<UserRole> toEntities(List<UserRoleDTO> userRoleDTOs) {
        List<UserRole> userRoles = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les DTOs en entités
        for (UserRoleDTO userRoleDTO : userRoleDTOs) {
            userRoles.add(toEntity(userRoleDTO));
        }

        return userRoles;
    }
}