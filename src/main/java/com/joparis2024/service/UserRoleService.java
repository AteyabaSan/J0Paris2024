package com.joparis2024.service;

import com.joparis2024.dto.UserRoleDTO;
import com.joparis2024.mapper.UserRoleMapper;
import com.joparis2024.model.Role;
import com.joparis2024.model.User;
import com.joparis2024.model.UserRole;
import com.joparis2024.repository.RoleRepository;
import com.joparis2024.repository.UserRepository;
import com.joparis2024.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRoleMapper userRoleMapper; // Le mapper pour convertir entre DTO et entités

    @Autowired
    private UserRepository userRepository; // Pour récupérer l'utilisateur
    @Autowired
    private RoleRepository roleRepository; // Pour récupérer le rôle

    // Créer une relation User_Role
    public UserRoleDTO createUserRole(UserRoleDTO userRoleDTO) throws Exception {
        User user = userRepository.findById(userRoleDTO.getUserId())
            .orElseThrow(() -> new Exception("Utilisateur non trouvé"));

        Role role = roleRepository.findById(userRoleDTO.getRoleId())
            .orElseThrow(() -> new Exception("Rôle non trouvé"));

        UserRole userRole = userRoleMapper.toEntity(userRoleDTO);
        userRole.setUser(user);  // Associe l'utilisateur trouvé
        userRole.setRole(role);  // Associe le rôle trouvé

        UserRole savedUserRole = userRoleRepository.save(userRole);
        return userRoleMapper.toDTO(savedUserRole);
    }

    // Mettre à jour une relation User_Role
    public UserRoleDTO updateUserRole(Long id, UserRoleDTO userRoleDTO) throws Exception {
        UserRole userRole = userRoleRepository.findById(id)
            .orElseThrow(() -> new Exception("Relation User_Role non trouvée"));

        User user = userRepository.findById(userRoleDTO.getUserId())
            .orElseThrow(() -> new Exception("Utilisateur non trouvé"));
        Role role = roleRepository.findById(userRoleDTO.getRoleId())
            .orElseThrow(() -> new Exception("Rôle non trouvé"));

        // Mettre à jour l'utilisateur et le rôle de la relation
        userRole.setUser(user);
        userRole.setRole(role);

        UserRole updatedUserRole = userRoleRepository.save(userRole);

        return userRoleMapper.toDTO(updatedUserRole);
    }

    // Supprimer une relation User_Role
    public void deleteUserRole(Long id) throws Exception {
        UserRole userRole = userRoleRepository.findById(id)
            .orElseThrow(() -> new Exception("Relation User_Role non trouvée"));
        
        userRoleRepository.delete(userRole);
    }

    // Récupérer toutes les relations User_Role
    public List<UserRoleDTO> getAllUserRoles() {
        List<UserRole> userRoles = userRoleRepository.findAll();
        return userRoleMapper.toDTOs(userRoles);  // Utiliser le mapper pour convertir la liste
    }
    
 // Ajouter la méthode pour récupérer une relation User_Role par ID
    public UserRoleDTO getUserRoleById(Long id) throws Exception {
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new Exception("Relation User_Role non trouvée"));
        return userRoleMapper.toDTO(userRole);
    }
}
