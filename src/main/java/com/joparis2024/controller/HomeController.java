package com.joparis2024.controller;


import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joparis2024.dto.CategoryDTO;
import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.OfferDTO;
import com.joparis2024.dto.OrderDTO;
import com.joparis2024.service.EventService;
import com.joparis2024.service.OfferService;
import com.joparis2024.service.OrderManagementFacade;
import com.joparis2024.service.PaymentManagementFacade;


@Controller
public class HomeController {

    @Autowired
    private PaymentManagementFacade paymentManagementFacade;

    @Autowired
    private OrderManagementFacade orderManagementFacade;
    
    @Autowired
    private OfferService offerService;
    
    @Autowired
    private EventService eventService;


    // Méthode pour afficher la page d'accueil
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Accueil - JO Paris 2024");
        return "home";  // Renvoie à la vue "home.html"
    }

    // Méthode pour l'inscription
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String email, @RequestParam String password, Model model) {
        // Logique d'inscription (simulée pour l'instant)
        model.addAttribute("message", "Inscription réussie !");
        return "login";  // Redirige vers la page de connexion après l'inscription
    }

    // Méthode pour afficher la liste des événements
    @GetMapping("/categories/{categorieId}/events")
    public String showEvents() {
        return "events";  // Vue des événements disponibles
    }

    // Méthode pour initier un paiement avec Stripe
    @PostMapping("/payment/initiate")
    public String initiatePayment(@RequestParam Long orderId, Model model) {
        try {
            String stripeSessionId = paymentManagementFacade.initiatePayment(orderId);
            model.addAttribute("sessionId", stripeSessionId);
            return "redirect:/checkout?session_id=" + stripeSessionId;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'initialisation du paiement.");
            return "error";
        }
    }

    // Méthode pour la page de confirmation de paiement
    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam("session_id") String sessionId, Model model) {
        try {
            paymentManagementFacade.confirmPayment(sessionId);
            model.addAttribute("message", "Paiement confirmé avec succès !");
            return "paymentConfirmation";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la confirmation du paiement.");
            return "error";
        }
    }

    // Méthode pour afficher la page de récapitulatif de commande
    @GetMapping("/order/summary")
    public String orderSummary(@RequestParam Long orderId, Model model) {
        try {
            OrderDTO order = orderManagementFacade.getOrderWithDetails(orderId);
            model.addAttribute("order", order);
            return "orderSummary";  // Vue pour afficher le récapitulatif
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors du chargement de la commande.");
            return "error";
        }
    }
    
    @GetMapping("/categories")
    public String getCategories(Model model) {
        // Ajouter des informations de catégories au modèle
        // Ce serait mieux de récupérer les catégories depuis une base de données, 
        // mais ici je vais simplement simuler avec une liste statique pour l'exemple.
        
        List<CategoryDTO> categories = Arrays.asList(
            new CategoryDTO(1L, "Athlétisme", "Stade De France - Saint-Denis"),
            new CategoryDTO(3L, "Aviron", "Vaires-sur-Marne - Seine et Marne"),
            new CategoryDTO(4L, "Natation", "Centre Aquatique - Saint-Denis"),
            new CategoryDTO(5L, "Escrime", "Grand Palais - Paris Centre"),
            new CategoryDTO(6L, "Gymnastique", "Bercy Arena - Accor Arena"),
            new CategoryDTO(7L, "Boxe", "Porte de la Chapelle Arena"),
            new CategoryDTO(8L, "Basketball", "Porte de la Chapelle Arena"),
            new CategoryDTO(9L, "Football", "Parc des Princes"),
            new CategoryDTO(10L, "Rugby", "Stade de France"),
            new CategoryDTO(11L, "Cyclisme", "Champs Elysées - Colline d'Elancourt"),
            new CategoryDTO(12L, "Tennis", "Rolland-Garros"),
            new CategoryDTO(13L, "Volleyball", "Parc des expositions Versailles / Champs de Mars-Tour Eiffel")
        );

        model.addAttribute("categories", categories);
        return "categories"; // Retourne la vue Thymeleaf
    }
    
    @GetMapping("/events/{eventId}/offers")
    public String getOffersByEvent(@PathVariable Long eventId, Model model) {
        try {
            EventDTO event = eventService.getEventById(eventId);  // Récupère l'événement par son ID
            List<OfferDTO> offers = offerService.getOffersByEvent(eventId);  // Récupère les offres associées à cet événement
            
            model.addAttribute("event", event);  // Ajoute l'événement au modèle
            model.addAttribute("offers", offers);  // Ajoute les offres au modèle
            return "offers";  // Le nom du template Thymeleaf
        } catch (Exception e) {
            // En cas d'erreur (par exemple, si l'événement n'existe pas), on redirige vers une page d'erreur
            model.addAttribute("errorMessage", "L'événement sélectionné n'existe pas ou une erreur est survenue.");
            return "error";  // Le nom de la page d'erreur
        }
    }

}