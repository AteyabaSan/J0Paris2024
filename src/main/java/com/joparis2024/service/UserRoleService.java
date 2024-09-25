package com.joparis2024.service;

import com.joparis2024.dto.UserRoleDTO;
import com.joparis2024.model.UserRole;
import com.joparis2024.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    // Créer une relation User_Role
    public UserRole createUserRole(UserRoleDTO userRoleDTO) throws Exception {
        UserRole userRole = new UserRole();
        userRole.setUser(userRoleDTO.getUser()); // mapper le User
        userRole.setRole(userRoleDTO.getRole()); // mapper le Role
        return userRoleRepository.save(userRole);
    }

    // Mettre à jour une relation User_Role
    public UserRole updateUserRole(Long id, UserRoleDTO userRoleDTO) throws Exception {
        Optional<UserRole> userRoleOpt = userRoleRepository.findById(id);
        if (!userRoleOpt.isPresent()) {
            throw new Exception("Relation User_Role non trouvée");
        }

        UserRole userRole = userRoleOpt.get();
        userRole.setUser(userRoleDTO.getUser());
        userRole.setRole(userRoleDTO.getRole());
        return userRoleRepository.save(userRole);
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
    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }
}
