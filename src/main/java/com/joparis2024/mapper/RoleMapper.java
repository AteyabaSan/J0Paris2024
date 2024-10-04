package com.joparis2024.mapper;

import org.springframework.stereotype.Component;
import com.joparis2024.dto.RoleDTO;
import com.joparis2024.model.Role;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleMapper {

    // Mapper Role -> RoleDTO
    public RoleDTO toDTO(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }

    // Mapper RoleDTO -> Role
    public Role toEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        return role;
    }

    // Mapper une liste de Role -> une liste de RoleDTO
    public List<RoleDTO> toDTOs(List<Role> roles) {
        List<RoleDTO> roleDTOs = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les entités en DTOs
        for (Role role : roles) {
            roleDTOs.add(toDTO(role));
        }
        return roleDTOs;
    }
}
