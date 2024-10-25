package com.joparis2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.joparis2024.model.User;
import com.joparis2024.repository.UserRepository;
import com.joparis2024.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Récupère l'utilisateur en utilisant le username
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        // Retourne un CustomUserDetails qui inclut l'email et les rôles
        return new CustomUserDetails(user);
    }
    
    // Méthode pour récupérer l'email de l'utilisateur connecté
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getEmail(); // Retourne l'email de l'utilisateur
        }

        throw new UsernameNotFoundException("Utilisateur non authentifié");
    }
}
