package com.joparis2024.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    // Méthode pour afficher la page d'accueil
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Accueil - JO Paris 2024");
        return "home"; // Cela renvoie la vue "home.html"
    }

    // Méthode pour afficher la page de connexion
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        // Ajoutez votre logique de connexion ici
        model.addAttribute("message", "Connexion réussie !");
        return "dashboard"; // Redirige vers le tableau de bord après connexion
    }

    // Méthode pour afficher la page d'inscription
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String email, @RequestParam String password, Model model) {
        // Ajoutez votre logique d'inscription ici
        model.addAttribute("message", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
        return "login"; // Redirige vers la page de connexion après l'inscription
    }


    // Méthode pour afficher le tableau de bord
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Tableau de bord - JO Paris 2024");
        return "dashboard"; // Cela renvoie la vue "dashboard.html"
    }
}