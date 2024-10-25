package com.joparis2024.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.joparis2024.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/home", "/login", "/register", "/css/**", "/js/**", "/categories/**", "/events/**", "/offers/**").permitAll()
                .requestMatchers("/order-recap", "/order-confirmation", "/orders/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/payment/create-session").permitAll()  // Autoriser POST sur /payment/create-session
                .requestMatchers(HttpMethod.GET, "/payment").permitAll()  // Autoriser GET sur /payment (la page de paiement)
                .requestMatchers(HttpMethod.GET, "/payment-confirmation").permitAll()  // Autoriser GET sur /payment-confirmation
                .requestMatchers(HttpMethod.GET, "/payment/success").permitAll()  // Autoriser GET sur /payment/success
                .requestMatchers(HttpMethod.GET, "/ticket-confirmation").permitAll()  // Autoriser GET sur /ticket-confirmation (nouvelle page)
                .requestMatchers(HttpMethod.POST, "/send-tickets").permitAll()  // Autoriser POST sur /send-tickets pour l'envoi de tickets
                .requestMatchers(HttpMethod.POST, "/test-stripe-session").permitAll()
                .requestMatchers("https://checkout.stripe.com/**").permitAll()  // Autoriser l'accès à Stripe
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .permitAll()
            )
            .rememberMe((rememberMe) -> rememberMe
                .tokenValiditySeconds(86400)
                .key("uniqueAndSecret")
            )
            .csrf(csrf -> csrf.disable())  // Désactivation de CSRF pour simplifier les tests
            .userDetailsService(userDetailsService);  // Utilisation de CustomUserDetailsService

        return http.build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}