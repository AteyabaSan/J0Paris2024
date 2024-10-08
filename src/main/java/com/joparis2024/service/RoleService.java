package com.joparis2024.service;

import com.joparis2024.dto.RoleDTO;
import com.joparis2024.mapper.RoleMapper;
import com.joparis2024.model.Role;
import com.joparis2024.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;  // Injection du RoleMapper

    // Créer un rôle
    public RoleDTO createRole(RoleDTO roleDTO) throws Exception {
        if (roleRepository.findByName(roleDTO.getName()) != null) {
            throw new Exception("Le rôle existe déjà");
        }

        Role role = roleMapper.toEntity(roleDTO);  // Utilisation du RoleMapper pour le mapping
        Role savedRole = roleRepository.save(role);

        return roleMapper.toDTO(savedRole);  // Retourner un DTO via le RoleMapper
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

        return roleMapper.toDTO(updatedRole);
    }

    // Récupérer tous les rôles
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleDTO> roleDTOs = new ArrayList<>();
        // Utilisation d'une boucle pour transformer chaque rôle en DTO
        for (Role role : roles) {
            roleDTOs.add(roleMapper.toDTO(role));
        }
        return roleDTOs;
    }

    // Récupérer un rôle par ID
    public RoleDTO getRoleById(Long roleId) throws Exception {
        Optional<Role> role = roleRepository.findById(roleId);
        if (!role.isPresent()) {
            throw new Exception("Rôle non trouvé");
        }
        return roleMapper.toDTO(role.get());
    }

    // Supprimer un rôle
    public void deleteRole(Long roleId) throws Exception {
        Optional<Role> role = roleRepository.findById(roleId);
        if (!role.isPresent()) {
            throw new Exception("Rôle non trouvé");
        }
        roleRepository.delete(role.get());
    }
    
    public List<Role> getRolesByNames(List<String> roleNames) {
        return roleRepository.findByNameIn(roleNames);
    }

}
