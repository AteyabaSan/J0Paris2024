package com.joparis2024.service;

import com.joparis2024.dto.RoleDTO;
import com.joparis2024.model.Role;
import com.joparis2024.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Créer un rôle
    public RoleDTO createRole(RoleDTO roleDTO) throws Exception {
        if (roleRepository.findByName(roleDTO.getName()) != null) {
            throw new Exception("Le rôle existe déjà");
        }

        Role role = mapToEntity(roleDTO);
        Role savedRole = roleRepository.save(role);

        return mapToDTO(savedRole);
    }

    // Mettre à jour un rôle
    public RoleDTO updateRole(Long roleId, RoleDTO roleDTO) throws Exception {
        Optional<Role> existingRole = roleRepository.findById(roleId);
        if (!existingRole.isPresent()) {
            throw new Exception("Rôle non trouvé");
        }

        Role role = existingRole.get();
        role.setName(roleDTO.getName());
        Role updatedRole = roleRepository.save(role);

        return mapToDTO(updatedRole);
    }

    // Récupérer tous les rôles
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Récupérer un rôle par ID
    public RoleDTO getRoleById(Long roleId) throws Exception {
        Optional<Role> role = roleRepository.findById(roleId);
        if (!role.isPresent()) {
            throw new Exception("Rôle non trouvé");
        }
        return mapToDTO(role.get());
    }

    // Supprimer un rôle
    public void deleteRole(Long roleId) throws Exception {
        Optional<Role> role = roleRepository.findById(roleId);
        if (!role.isPresent()) {
            throw new Exception("Rôle non trouvé");
        }
        roleRepository.delete(role.get());
    }

    // Mapper Role -> RoleDTO
    private RoleDTO mapToDTO(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }

    // Mapper RoleDTO -> Role
    private Role mapToEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        return role;
    }
}
