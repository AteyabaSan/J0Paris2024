package com.joparis2024.security;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration
public class SecurityConfig {

//    @Bean
//    UserDetailsService userDetailsService() {
//        // Création d'un utilisateur en mémoire pour le test
//        UserDetails user = User.withUsername("ateyaba")
//                .password(passwordEncoder().encode("EspritNwaar74!"))
//                .roles("USER")
//                .build();
//        
//        return new InMemoryUserDetailsManager(user);
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        // Utilisation de BCrypt pour l'encodage des mots de passe
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // Configuration de Spring Security pour restreindre l'accès aux utilisateurs authentifiés
//        http
//            .authorizeHttpRequests(authorizeRequests -> 
//                authorizeRequests.anyRequest().anonymous()  );
//     
//        
//        return http.build();
//    }
}
