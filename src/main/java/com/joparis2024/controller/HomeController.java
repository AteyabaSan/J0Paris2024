package com.joparis2024.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Méthode pour afficher la page d'accueil
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Accueil - JO Paris 2024");
        return "home"; // Cela renvoie la vue "home.html"
    }

    // Méthode pour afficher la page de connexion
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Connexion - JO Paris 2024");
        return "login"; // Cela renvoie la vue "login.html"
    }

    // Méthode pour afficher la page d'inscription
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Inscription - JO Paris 2024");
        return "register"; // Cela renvoie la vue "register.html"
    }

    // Méthode pour afficher le tableau de bord
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Tableau de bord - JO Paris 2024");
        return "dashboard"; // Cela renvoie la vue "dashboard.html"
    }
}
