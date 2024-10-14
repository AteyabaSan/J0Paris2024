package com.joparis2024.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joparis2024.model.Role;
import com.joparis2024.model.User;
import com.joparis2024.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	List<UserRole> findByUser (User user);
	boolean existsByUserAndRole(User user, Role role);

}