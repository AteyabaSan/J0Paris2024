package com.joparis2024.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    // Méthode pour trouver les rôles par leurs noms
    List<Role> findByNameIn(List<String> names);
    
    // Méthode pour trouver un rôle par son nom
    Role findByName(String name);
}
