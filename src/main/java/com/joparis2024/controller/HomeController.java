package com.joparis2024.controller;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.joparis2024.mapper.TicketMapper;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.TicketRepository;
import com.joparis2024.security.CustomUserDetails;
import com.joparis2024.service.EmailService;
import com.joparis2024.service.EventManagementFacade;
import com.joparis2024.service.EventService;
import com.joparis2024.service.OfferService;
//import com.joparis2024.service.OrderManagementFacade;
import com.joparis2024.service.OrderService;
import com.joparis2024.service.PaymentManagementFacade;
import com.joparis2024.service.RoleService;
//import com.joparis2024.service.StripeService;
import com.joparis2024.service.TicketService;
import com.joparis2024.service.UserService;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



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
    
//    @Autowired
//    private StripeService stripeService;
    
    @Autowired
    private PaymentManagementFacade paymentManagementFacade;
    
    @Autowired
    private EmailService emailService;
    
//    @Autowired
//    private OrderManagementFacade orderManagementFacade;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private EventManagementFacade eventManagementFacade;
    
    @Autowired
    private TicketMapper ticketMapper;
    
    @Autowired
    private TicketRepository ticketRepository;
    
    // Injecter la clé API Stripe
    @Value("${stripe.api.key.secret-key}")
    private String stripeApiKey;
    
    
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
 // Méthode pour afficher la page d'accueil
    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            
            // Récupérer les détails de l'utilisateur
            if (auth.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();

                // Récupérer le username (pour l'authentification)
                String username = userDetails.getUsername();
                model.addAttribute("username", username);

                // Récupérer l'email via une classe UserDetails personnalisée
                if (userDetails instanceof CustomUserDetails) {
                    CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
                    String email = customUserDetails.getEmail();  // Si ton CustomUserDetails a un champ email
                    model.addAttribute("userEmail", email);
                }
            }

            // Récupérer les rôles
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            model.addAttribute("userRoles", authorities);
        }

        model.addAttribute("title", "Accueil - JO Paris 2024");
        return "home";  // Renvoie à la vue home.html
    }

    // Redirection de l'URL racine vers /home
    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

    // Méthode pour l'inscription de l'utilisateur
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDTO userDTO, Model model) {
        try {
            // Ajouter le rôle USER
            RoleDTO userRole = roleService.findByName("USER");
            userDTO.getRoles().add(userRole.getName());
            userDTO.setEnabled(true);

            // Sauvegarder l'utilisateur
            userService.save(userDTO);

            // Authentifier automatiquement après inscription
            UsernamePasswordAuthenticationToken auth = 
                new UsernamePasswordAuthenticationToken(userDTO, null, userDTO.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            return "redirect:/home";
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Un utilisateur avec cet email existe déjà")) {
                model.addAttribute("errorMessage", "Cet email est déjà utilisé.");
            } else {
                model.addAttribute("errorMessage", "Une erreur s'est produite.");
            }
            return "register";
        }
    }

    // Afficher la page d'inscription
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    // Afficher la liste des événements d'une catégorie
    @GetMapping("/categories/{categoryId}/events")
    public String showEvents(@PathVariable Long categoryId, HttpSession session, Model model) {
        try {
            // Log pour voir la catégorie sauvegardée en session
            session.setAttribute("selectedCategory", categoryId);
            logger.info("Catégorie sélectionnée dans la session: {}", categoryId);
            
            return "events";  // Vue des événements après sélection
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la récupération des événements.");
            return "error";
        }
    }


    // Création d'une session Stripe
    @PostMapping("/payment/create-session")
    public String createCheckoutSession(HttpSession session, Model model) {
        try {
            // Définir la clé API Stripe
            Stripe.apiKey = stripeApiKey;

            // Utilisation temporaire de valeurs statiques pour la session Stripe
            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8081/payment/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8081/payment/cancel")
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmount(15000L)  // 10 EUR statique pour la démo
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Veuillez Rentrer Vos Informations de Paiement : ")  // Nom statique pour la démo
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build();

            // Créer la session Stripe
            Session stripeSession = Session.create(params);

            // Stocker l'ID de la session Stripe dans la session HTTP
            session.setAttribute("stripeSessionId", stripeSession.getId());

            // Rediriger l'utilisateur vers Stripe Checkout
            return "redirect:" + stripeSession.getUrl();

        } catch (Exception e) {
            logger.error("Erreur lors de la création de la session de paiement Stripe : ", e);
            model.addAttribute("errorMessage", "Erreur lors de la création de la session de paiement.");
            return "error";
        }
    }








    // Afficher les catégories
    @GetMapping("/categories")
    public String getCategories(HttpSession session, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String userEmail = auth.getName();
            System.out.println("Utilisateur connecté dans /categories : " + userEmail);  // Log l'utilisateur connecté
        }
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

    // Récupérer les offres pour un événement spécifique
    @GetMapping("/events/{eventId}/offers")
    public String getOffersByEvent(@PathVariable Long eventId, Model model, HttpSession session) {
        try {
            // Récupère l'événement par son ID
            EventDTO event = eventService.getEventById(eventId);
            
            // Log pour voir l'événement récupéré
            logger.info("Événement récupéré: {}", event != null ? event.getId() : "null");

            // Récupère les offres associées à cet événement
            List<OfferDTO> offers = offerService.getOffersByEvent(eventId);
            logger.info("Offres récupérées: {}", offers.size());

            // Sauvegarde l'événement dans la session
            session.setAttribute("selectedEvent", event);
            logger.info("Événement sauvegardé en session: {}", event.getId());

            // Ajoute les données au modèle pour la vue
            model.addAttribute("event", event);
            model.addAttribute("offers", offers);

            // Renvoie la vue pour afficher les offres
            return "offers";
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'événement ou des offres: {}", e.getMessage());
            model.addAttribute("errorMessage", "Erreur lors de la récupération de l'événement ou des offres.");
            return "error";
        }
    }


 // Sélectionner une offre et enregistrer les informations dans la session
    @GetMapping("/events/{eventId}/offers/select")
    public String selectOffer(@PathVariable Long eventId, @RequestParam Long offerId, HttpSession session) {
        try {
            // Récupération des informations complètes de l'événement et des tickets
            logger.info("Tentative de récupération des détails pour l'événement ID: {}", eventId);
            EventDTO event = eventManagementFacade.getEventWithTicketDetails(eventId);
            if (event == null) {
                logger.error("Événement introuvable pour l'ID: {}", eventId);
                throw new EntityNotFoundException("Événement introuvable.");
            }
            logger.info("Événement récupéré: {}", event.getId());

            // Récupération de l'offre sélectionnée
            logger.info("Tentative de récupération de l'offre ID: {}", offerId);
            OfferDTO selectedOffer = offerService.getOfferById(offerId);
            if (selectedOffer == null) {
                logger.error("Offre introuvable pour l'ID: {}", offerId);
                throw new EntityNotFoundException("Offre introuvable.");
            }
            logger.info("Offre sélectionnée: {}", selectedOffer.getId());

            // Stocker les informations complètes en session avec des clés cohérentes
            session.setAttribute("selectedEventId", event.getId());
            session.setAttribute("selectedOfferId", selectedOffer.getId());

            logger.info("Événement et offre stockés en session. Event ID: {}, Offer ID: {}", eventId, offerId);

            // Rediriger vers la page de récapitulatif de commande
            return "redirect:/order-recap";
        } catch (Exception e) {
            logger.error("Erreur lors de la sélection de l'offre ou de l'événement: {}", e.getMessage());
            // Gestion des erreurs
            return "redirect:/error";  // Redirige vers une page d'erreur ou autre gestion
        }
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
    
 // Afficher la page de récapitulatif de la commande
    @GetMapping("/order-recap")
    public String getOrderRecap(Model model, HttpSession session) {
        // Vérification de l'authentification
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            session.setAttribute("redirectUrl", "/order-recap");
            logger.info("Utilisateur non authentifié. Redirection vers la page de login.");
            return "redirect:/login";
        }

        // Récupération des IDs de la session
        Long selectedEventId = (Long) session.getAttribute("selectedEventId");
        Long selectedOfferId = (Long) session.getAttribute("selectedOfferId");

        logger.info("Données récupérées depuis la session: Event ID = {}, Offer ID = {}", selectedEventId, selectedOfferId);

        if (selectedEventId == null || selectedOfferId == null) {
            logger.warn("Données manquantes dans la session : Event ID ou Offer ID null. Redirection vers les catégories.");
            return "redirect:/categories";  // Rediriger si les données sont manquantes
        }

        try {
            // Récupérer les détails de l'événement avec les tickets et leurs prix via EventManagementFacade
            EventDTO event = eventManagementFacade.getEventWithTicketDetails(selectedEventId);
            OfferDTO offer = offerService.getOfferById(selectedOfferId);  // Utiliser le service pour récupérer l'offre

            logger.info("Détails de l'événement et de l'offre récupérés : Event ID = {}, Offer ID = {}", 
                event != null ? event.getId() : "null", 
                offer != null ? offer.getId() : "null");

            if (event == null || offer == null) {
                logger.warn("L'événement ou l'offre est introuvable. Redirection vers les catégories.");
                return "redirect:/categories";  // Rediriger si les données sont manquantes
            }

            // Calcul du montant total à partir des tickets et de l'offre
            double totalAmount = orderService.calculateTotalPrice(event.getTickets(), offer);
            logger.info("Montant total calculé pour la commande : {}", totalAmount);

            // Ajout des données au modèle pour la vue
            model.addAttribute("event", event);
            model.addAttribute("offer", offer);
            model.addAttribute("totalAmount", totalAmount);

            return "order-recap";

        } catch (Exception e) {
            // Gérer l'erreur, loguer l'exception et rediriger vers une page d'erreur
            logger.error("Erreur lors du traitement de la commande", e);
            model.addAttribute("errorMessage", "Une erreur est survenue lors du traitement de la commande.");
            return "error";  // Afficher une page d'erreur ou rediriger
        }
    }

 // Confirmation de commande
    @PostMapping("/confirm-order")
    public String confirmOrder(HttpSession session) {
        try {
            // Récupérer les objets EventDTO et OfferDTO depuis la session si disponibles
            EventDTO event = (EventDTO) session.getAttribute("selectedEvent");
            OfferDTO offer = (OfferDTO) session.getAttribute("selectedOffer");

            // Si les objets ne sont pas dans la session, les récupérer via leurs IDs
            if (event == null || offer == null) {
                Long selectedEventId = (Long) session.getAttribute("selectedEventId");
                Long selectedOfferId = (Long) session.getAttribute("selectedOfferId");

                logger.info("Récupéré de la session - selectedEventId: {}, selectedOfferId: {}", selectedEventId, selectedOfferId);

                // Récupérer les détails de l'événement et de l'offre
                if (event == null) {
                    event = eventManagementFacade.getEventWithTicketDetails(selectedEventId);
                    logger.info("Événement récupéré : {}", event);
                }
                if (offer == null) {
                    offer = offerService.getOfferById(selectedOfferId);
                    logger.info("Offre récupérée : {}", offer);
                }

                // Vérifier que les données sont valides
                if (event == null || offer == null) {
                    logger.warn("Événement ou offre est null. Redirection vers /categories.");
                    return "redirect:/categories";
                }
            }

            // Vérifier si des tickets existent déjà pour cet événement et cette offre
            List<Ticket> existingTickets = ticketRepository.findByEventIdAndOfferId(event.getId(), offer.getId());

            List<TicketDTO> tickets;
            if (existingTickets.isEmpty()) {
                // Si aucun ticket n'existe, créer des tickets manuellement en fonction de l'offre
                logger.warn("Aucun ticket trouvé pour l'événement {}. Création manuelle des tickets en fonction de l'offre.", event.getEventName());

                tickets = new ArrayList<>();
                int numberOfTickets = 0;

                switch (offer.getName().toLowerCase()) {
                    case "solo":
                        numberOfTickets = 1;
                        break;
                    case "duo":
                        numberOfTickets = 2;
                        break;
                    case "familial":
                        numberOfTickets = 4;
                        break;
                    default:
                        logger.error("Offre non reconnue : {}", offer.getName());
                        return "redirect:/error";
                }

                // Création des tickets en fonction de l'offre
                for (int i = 0; i < numberOfTickets; i++) {
                    TicketDTO ticket = new TicketDTO();
                    ticket.setEvent(event);  // Associer l'événement
                    Double offerPrice = offer.getPrice();  // Vérifie et récupère le prix de l'offre
                    if (offerPrice == null) {
                        logger.error("Le prix pour l'offre {} est null. Utilisation d'un prix par défaut.", offer.getName());
                        offerPrice = 150.0;  // Valeur par défaut si le prix est null
                    }
                    ticket.setPrice(offerPrice);  // Utiliser le prix de l'offre pour chaque ticket
                    ticket.setAvailable(true);  // Les tickets sont disponibles
                    ticket.setQuantity(1);  // Chaque ticket représente une place

                    tickets.add(ticket);
                    logger.info("Création d'un ticket pour l'événement {} avec le prix {}", event.getEventName(), ticket.getPrice());
                }

                // Sauvegarder les tickets en base de données
                for (TicketDTO ticketDTO : tickets) {
                    // Utilise un mapper pour convertir TicketDTO en Ticket
                    Ticket ticket = ticketMapper.toEntity(ticketDTO);
                    Ticket savedTicket = ticketService.createTicket(ticket);
                    logger.info("Ticket ID {} sauvegardé avec succès", savedTicket.getId());
                }

                // Ajouter les tickets à l'événement
                event.setTickets(tickets);

            } else {
                // Si des tickets existent déjà, les convertir en DTO et les utiliser
                tickets = ticketMapper.toDTOs(existingTickets);
                logger.info("Tickets récupérés de la base de données pour l'événement {} et l'offre {}", event.getEventName(), offer.getName());
            }

            // Créer une nouvelle commande
            OrderDTO order = new OrderDTO();
            order.setEvent(event);
            order.setOffer(offer);
            order.setTotalAmount(orderService.calculateTotalPrice(tickets, offer));

            // Définir la date de la commande (solution pour l'erreur NOT NULL)
            order.setOrderDate(LocalDateTime.now());

            // Définir le statut de la commande (par exemple PENDING)
            order.setStatus("PENDING");  // Utiliser le champ String existant pour le statut

            // Log de la commande avant enregistrement
            logger.info("Nouvelle commande créée avec totalAmount: {} pour l'événement: {} et l'offre: {}", order.getTotalAmount(), event.getEventName(), offer.getName());

            // Enregistrer la commande
            orderService.createOrder(order);
            logger.info("Commande enregistrée avec succès");
            
            // Ajouter l'OrderDTO dans la session
            session.setAttribute("currentOrder", order);  // Ajoutez cette ligne
            // Mettre à jour les objets dans la session pour Stripe ou d'autres usages
            session.setAttribute("selectedEvent", event);
            session.setAttribute("selectedOffer", offer);
            logger.info("Événement et offre sauvegardés en session");

            // Nettoyer les identifiants de la session si plus nécessaires
            if (session.getAttribute("selectedEventId") != null) {
                session.removeAttribute("selectedEventId");
                logger.info("Identifiant selectedEventId supprimé de la session");
            }
            if (session.getAttribute("selectedOfferId") != null) {
                session.removeAttribute("selectedOfferId");
                logger.info("Identifiant selectedOfferId supprimé de la session");
            }

            // Rediriger vers la confirmation de commande avec succès
            return "redirect:/order-confirmation?success";
        } catch (Exception e) {
            // Log en cas d'erreur
            logger.error("Erreur lors de la confirmation de la commande : {}", e.getMessage(), e);
            return "redirect:/error";  // Redirection vers une page d'erreur si une exception survient
        }
    }

    @GetMapping("/order-confirmation")
    public String showOrderConfirmationPage(Model model, HttpSession session, HttpServletRequest request) {
        // Récupérer les objets EventDTO et OfferDTO depuis la session
        EventDTO event = (EventDTO) session.getAttribute("selectedEvent");
        OfferDTO offer = (OfferDTO) session.getAttribute("selectedOffer");

        // Si les objets ne sont pas dans la session, rediriger vers une autre page
        if (event == null || offer == null) {
            // Si l'événement ou l'offre ne sont pas trouvés, rediriger vers une page d'erreur ou de sélection de catégorie
            return "redirect:/categories"; 
        }

        // Ajouter les informations de l'événement et de l'offre au modèle pour les afficher dans la vue
        model.addAttribute("event", event);
        model.addAttribute("offer", offer);

        // Récupérer le paramètre 'success' depuis la requête
        String success = request.getParameter("success");

        // Si le paramètre 'success' est présent, ajouter un message au modèle
        if (success != null) {
            model.addAttribute("message", "Votre commande a été confirmée avec succès !");
        }

        // Renvoyer la vue de confirmation (Thymeleaf)
        return "order-confirmation";
    }


    // Confirmation de paiement via Stripe
    @GetMapping("/payment/success")
    public String paymentSuccess(HttpSession session, @RequestParam("session_id") String sessionId, Model model) {
        try {
            // Confirmer le paiement via Stripe
            paymentManagementFacade.confirmPayment(sessionId);

            // Récupérer la commande liée à la session
            OrderDTO order = (OrderDTO) session.getAttribute("currentOrder");

            if (order != null) {
                // Envoyer les billets par email avec des QR codes
                emailService.sendTicket(orderMapper.toEntity(order));
            } else {
                logger.warn("Aucune commande trouvée dans la session. Utilisation d'une commande fictive pour la démo.");
                // Si aucune commande n'est trouvée, tu peux générer des tickets fictifs pour la démo
                OrderDTO demoOrder = createDemoOrder();  // Crée une commande de démo
                emailService.sendTicket(orderMapper.toEntity(demoOrder));
            }

            // Rediriger vers la page de confirmation du paiement
            return "redirect:/payment-confirmation";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la confirmation du paiement.");
            return "error";
        }
    }

    private OrderDTO createDemoOrder() {
        // Créer une commande fictive pour la démo
        OrderDTO demoOrder = new OrderDTO();
        
        // Création d'un EventDTO fictif
        EventDTO event = new EventDTO();
        event.setId(1L);
        event.setEventName("Démonstration Événement");
        event.setLocation("Demo Location");
        demoOrder.setEvent(event);

        // Création d'une OfferDTO fictive
        OfferDTO offer = new OfferDTO();
        offer.setId(1L);
        offer.setName("Demo Offer");
        offer.setPrice(100.0);
        demoOrder.setOffer(offer);
        
        demoOrder.setTotalAmount(100.0);
        demoOrder.setOrderDate(LocalDateTime.now());
        demoOrder.setStatus("CONFIRMED");
        
        return demoOrder;
    }

    @GetMapping("/payment-confirmation")
    public String showPaymentConfirmation(Model model) {
        model.addAttribute("message", "Paiement confirmé avec succès.");
        return "payment-confirmation";  // Renvoyer vers la vue Thymeleaf payment-confirmation.html
    }


    // Page de login
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Retournera la vue login.html
    }

    // Page de profil utilisateur
    @GetMapping("/profile")
    public String getProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getEmail();
        model.addAttribute("email", email);
        return "profile";
    }
}