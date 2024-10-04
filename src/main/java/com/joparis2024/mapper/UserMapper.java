package com.joparis2024.mapper;

import org.springframework.stereotype.Component;
import com.joparis2024.dto.UserDTO;
import com.joparis2024.model.User;
import com.joparis2024.model.Role;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    // Mapper User -> UserDTO
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        List<String> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getName()); // Ajout du nom des rôles à la liste
        }

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles, // Liste des noms des rôles
                user.getEnabled(),
                user.getPhoneNumber(),
                user.getPassword() // Le mot de passe n'est normalement pas exposé
        );
    }

    // Mapper UserDTO -> User (avec une liste de rôles)
    public User toEntity(UserDTO userDTO, List<Role> roles) throws Exception {
        if (userDTO == null) {
            throw new Exception("Le UserDTO à mapper est nul.");
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(userDTO.getEnabled());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword()); // Le mot de passe est à gérer en toute sécurité
        user.setRoles(roles); // On affecte directement la liste des rôles fournie

        return user;
    }

 // Nouvelle méthode sans les rôles
    public User toEntity(UserDTO userDTO) throws Exception {
        if (userDTO == null) {
            throw new Exception("Le UserDTO à mapper est nul.");
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(userDTO.getEnabled());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword());

        // On ne définit pas les rôles ici
        return user;
    }
    
    // Mapper une liste de User -> une liste de UserDTO
    public List<UserDTO> toDTOs(List<User> users) {
        if (users == null || users.isEmpty()) {
            return new ArrayList<>();
        }

        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(toDTO(user)); // Transformation en boucle classique
        }

        return userDTOs;
    }

    // Mapper une liste de UserDTO -> une liste de User (avec une liste de rôles)
    public List<User> toEntities(List<UserDTO> userDTOs, List<Role> roles) throws Exception {
        if (userDTOs == null || userDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<User> users = new ArrayList<>();
        for (UserDTO userDTO : userDTOs) {
            users.add(toEntity(userDTO, roles)); // Transformation en boucle classique
        }

        return users;
    }
}