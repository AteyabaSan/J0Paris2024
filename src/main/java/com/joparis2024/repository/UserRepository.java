package com.joparis2024.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // Optionnel : Méthode pour vérifier si un utilisateur a un rôle spécifique
    boolean existsByRoles_Name(String roleName);
}
