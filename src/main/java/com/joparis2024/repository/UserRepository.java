package com.joparis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
