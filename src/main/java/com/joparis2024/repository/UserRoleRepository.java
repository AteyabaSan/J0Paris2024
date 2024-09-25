package com.joparis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
