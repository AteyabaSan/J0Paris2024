package com.joparis2024.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joparis2024.dto.CategoryDTO;
import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.OfferDTO;
import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.RoleDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.dto.UserDTO;
import com.joparis2024.mapper.OrderMapper;
import com.joparis2024.service.EmailService;
import com.joparis2024.service.EventService;
import com.joparis2024.service.OfferService;
import com.joparis2024.service.OrderManagementFacade;
import com.joparis2024.service.OrderService;
import com.joparis2024.service.PaymentManagementFacade;
import com.joparis2024.service.RoleService;
import com.joparis2024.service.StripeService;
import com.joparis2024.service.TicketService;
import com.joparis2024.service.UserService;
import com.stripe.exception.StripeException;

import jakarta.servlet.http.HttpSession;


@Controller
public class HomeController {
	
	@Autowired
	private TicketService ticketService;
	 
    @Autowired
    private OfferService offerService;
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private StripeService stripeService;
    
    @Autowired
    private PaymentManagementFacade paymentManagementFacade;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private OrderManagementFacade orderManagementFacade;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;


    // Méthode pour afficher la page d'accueil
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Accueil - JO Paris 2024");
        return "home";  // Renvoie à la vue "home.html"
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDTO userDTO, HttpSession session, Model model) {
        try {
            // Récupérer le rôle "USER" depuis la base de données
            RoleDTO userRole = roleService.findByName("USER");
            
            // Ajouter le rôle "USER" au DTO de l'utilisateur
            userDTO.getRoles().add(userRole.getName());
            
            // Assigner true à l'attribut enabled
            userDTO.setEnabled(true);
            
            // Sauvegarder l'utilisateur en base de données avec le rôle assigné
            UserDTO savedUser = userService.save(userDTO);

            // Sauvegarder l'utilisateur dans la session
            session.setAttribute("user", savedUser);

            // Rediriger vers la page d'accueil ou une autre page
            return "redirect:/home";
        } catch (Exception e) {
            // Gérer l'erreur, par exemple si l'email existe déjà
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }


    
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    // Méthode pour afficher la liste des événements
    @GetMapping("/categories/{categorieId}/events")
    public String showEvents() {
        return "events";  // Vue des événements disponibles
    }

    @PostMapping("/payment/create-session")
    public String createStripeSession(HttpSession session, Model model) {
        try {
            EventDTO selectedEvent = (EventDTO) session.getAttribute("selectedEvent");
            OfferDTO selectedOffer = (OfferDTO) session.getAttribute("selectedOffer");

            if (selectedEvent == null || selectedOffer == null) {
                return "error";
            }

            OrderDTO newOrder = orderService.createOrderFromSessionData(selectedEvent, selectedOffer);
            String sessionId = stripeService.createStripePaymentSession(orderMapper.toEntity(newOrder));

            System.out.println("ID de session Stripe : " + sessionId);

            return "redirect:https://checkout.stripe.com/pay/" + sessionId;
        } catch (StripeException e) {
            // Log spécifique pour Stripe
            e.printStackTrace();
            model.addAttribute("errorMessage", "Erreur lors de la création de la session Stripe : " + e.getMessage());
            return "error";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la création de la session de paiement.");
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
    public String getOffersByEvent(@PathVariable Long eventId, Model model, HttpSession session) {
        try {
            // Récupère l'événement par son ID
            EventDTO event = eventService.getEventById(eventId);
            
            // Récupère les offres associées à cet événement
            List<OfferDTO> offers = offerService.getOffersByEvent(eventId);

            // Sauvegarde l'événement dans la session
            session.setAttribute("selectedEvent", event);

            // Ajoute les données au modèle pour la vue
            model.addAttribute("event", event);
            model.addAttribute("offers", offers);

            // Renvoie la vue pour afficher les offres
            return "offers";
        } catch (Exception e) {
            // Gérer les erreurs et rediriger vers une page d'erreur
            model.addAttribute("errorMessage", "Erreur lors de la récupération de l'événement ou des offres.");
            return "error";
        }
    }

    @GetMapping("/orders/create")
    public String selectOffer(@RequestParam Long offerId, HttpSession session) throws Exception {
        // Récupérer l'offre par son ID
        OfferDTO selectedOffer = offerService.getOfferById(offerId);
        
        // Sauvegarder l'offre sélectionnée dans la session
        session.setAttribute("selectedOffer", selectedOffer);

        // Rediriger vers la page de récapitulatif
        return "redirect:/order-recap";
    }
    
    @GetMapping("/events/{eventId}/tickets")
    public String showTickets(@PathVariable Long eventId, @RequestParam Long offerId, Model model) {
        try {
            // Récupérer l'événement sélectionné
            EventDTO event = eventService.getEventById(eventId);

            // Récupérer les tickets disponibles pour cet événement et cette offre
            List<TicketDTO> tickets = ticketService.getTicketsByEventAndOffer(eventId, offerId);

            // Ajouter les informations de l'événement et des tickets au modèle
            model.addAttribute("event", event);
            model.addAttribute("tickets", tickets);
            model.addAttribute("offer", offerId); // Ajouter l'offre au modèle pour l'utiliser dans le lien

            return "tickets"; // Vue qui affiche la disponibilité des tickets
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la récupération des tickets.");
            return "error";
        }
    }
    
    @GetMapping("/order-recap")
    public String getOrderRecap(Model model, HttpSession session) {
        EventDTO selectedEvent = (EventDTO) session.getAttribute("selectedEvent");
        OfferDTO selectedOffer = (OfferDTO) session.getAttribute("selectedOffer");
        TicketDTO selectedTicket = (TicketDTO) session.getAttribute("selectedTicket");

        // Vérification des objets dans la session
        if (selectedEvent == null || selectedOffer == null) {
            return "error";  // Vue d'erreur si l'événement ou l'offre est manquant
        }

        // Récupération de l'utilisateur connecté
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            // Si l'utilisateur n'est pas trouvé, retourner une vue d'erreur
            return "error";
        }

        OrderDTO order = (OrderDTO) session.getAttribute("order");
        if (order == null) {
            try {
                // Construction de l'OrderDTO avec les informations disponibles
                order = new OrderDTO();
                order.setEvent(selectedEvent);
                order.setOffer(selectedOffer);
                order.setUser(user);  // Ajouter l'utilisateur à l'OrderDTO

                if (selectedTicket != null) {
                    List<TicketDTO> tickets = new ArrayList<>();
                    tickets.add(selectedTicket);
                    order.setTickets(tickets);
                }

                // Appel à la méthode createOrderWithDetails dans OrderManagementFacade
                order = orderManagementFacade.createOrderWithDetails(order);
                session.setAttribute("order", order);  // Stocker la commande dans la session

            } catch (Exception e) {
                // Log de l'erreur et gestion
                System.out.println("Erreur lors de la création de la commande: " + e.getMessage());
                return "error";  // Redirection vers une vue d'erreur
            }
        }

        // Ajout des attributs au modèle pour la vue
        model.addAttribute("event", selectedEvent);
        model.addAttribute("offer", selectedOffer);
        model.addAttribute("order", order);
      
        if (selectedTicket != null) {
            model.addAttribute("ticket", selectedTicket);
        } else {
            model.addAttribute("ticketError", "No ticket information available.");
        }

        // Calculer le total basé sur les tickets de l'événement et l'offre
        double totalAmount = orderService.calculateTotalPrice(selectedEvent.getTickets(), selectedOffer);
        model.addAttribute("totalAmount", totalAmount);

        // Retourner la vue du récapitulatif de la commande
        return "order-recap";
    }

    
    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam("session_id") String sessionId, Model model) {
        try {
            // Confirmer le paiement via Stripe
            paymentManagementFacade.confirmPayment(sessionId);

            // Récupérer la commande liée à la session
            OrderDTO order = orderManagementFacade.getOrderBySessionId(sessionId);

            // Envoyer les billets par email
            emailService.sendTicket(orderMapper.toEntity(order));

            // Ajouter les informations nécessaires au modèle
            model.addAttribute("message", "Paiement confirmé et tickets envoyés à l'adresse email " + order.getUser().getEmail());
            model.addAttribute("order", order);
            model.addAttribute("event", order.getEvent());
            model.addAttribute("tickets", order.getTickets());

            return "paymentConfirmation";  // Vue de confirmation du paiement
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la confirmation du paiement.");
            return "error";
        }
    }


}