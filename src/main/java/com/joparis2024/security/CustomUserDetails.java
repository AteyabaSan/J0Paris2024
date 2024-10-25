package com.joparis2024.security;


import com.joparis2024.model.Role;
import com.joparis2024.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;

    private final String email;

    // Constructeur basé sur l'entité User
    public CustomUserDetails(User user) {
        super(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, convertRolesToAuthorities(user.getRoles()));
        this.email = user.getEmail();
    }

    public String getEmail() {
        return email;
    }

    // Méthode pour convertir les rôles en authorities sans stream
    private static Collection<? extends GrantedAuthority> convertRolesToAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
}