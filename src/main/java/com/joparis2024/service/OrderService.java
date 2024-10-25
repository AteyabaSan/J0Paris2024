package com.joparis2024.service;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.OfferDTO;
import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.OrderMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.joparis2024.model.Order;
import com.joparis2024.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;
    
    public OrderMapper getOrderMapper() {
        return orderMapper;
    }
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {
        logger.info("Création d'une nouvelle commande : {}", orderDTO);

        orderDTO.setTotalAmount(0.0); // Montant initial
        Order order = orderMapper.toEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDTO(savedOrder);
    }


    @Transactional
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) throws Exception {
        logger.info("Mise à jour de la commande ID: {}", orderId);

        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));

        existingOrder.setOrderDate(orderDTO.getOrderDate());
        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setTotalAmount(orderDTO.getTotalAmount());

        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapper.toDTO(updatedOrder);
    }


    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() throws Exception {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toDTOs(orders);
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long orderId) throws Exception {
        logger.info("Récupération de la commande avec ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));
        return orderMapper.toDTO(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) throws Exception {
        logger.info("Annulation de la commande avec ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));
        order.setStatus("ANNULÉ");
        orderRepository.save(order);  // Mise à jour du statut
    }
    
    public Order findOrderEntityById(Long orderId) throws Exception {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new Exception("Commande non trouvée"));
    }
    
    @Transactional
    public Order updateOrder(Long orderId, Order order) throws Exception {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Commande non trouvée."));

        existingOrder.setOrderDate(order.getOrderDate());
        existingOrder.setStatus(order.getStatus());
        existingOrder.setTotalAmount(order.getTotalAmount());

        return orderRepository.save(existingOrder);
    }
    
 // Méthode pour trouver une commande par l'ID de session Stripe
    public OrderDTO findByStripeSessionId(String stripeSessionId) {
        Order order = orderRepository.findByStripeSessionId(stripeSessionId);
        return orderMapper.toDTO(order);  // Conversion en OrderDTO
    }
    
 // Méthode pour sauvegarder une commande
    public OrderDTO saveOrder(OrderDTO order) throws Exception {
        // Logique pour sauvegarder la commande, ici on pourrait mapper l'objet DTO en entité et le sauvegarder
        Order savedOrder = orderRepository.save(orderMapper.toEntity(order));
        return orderMapper.toDTO(savedOrder);
    }

    
    // Méthode pour trouver une commande par ID et retourner un Order, pas un DTO
    public Order findOrderById(Long orderId) throws Exception {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new Exception("Order not found"));
    }
    
    public OrderDTO createOrderFromSessionData(EventDTO event, OfferDTO offer) throws Exception {
        OrderDTO order = new OrderDTO();
        
        // Associer l'événement et l'offre à la commande
        order.setEvent(event);
        order.setOffer(offer);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());

        // Les tickets sont déjà récupérés avec l'événement via EventManagementFacade
        List<TicketDTO> tickets = event.getTickets();
        if (tickets == null || tickets.isEmpty()) {
            throw new Exception("Aucun ticket disponible pour cet événement.");
        }

        order.setTickets(tickets);

        // Calculer le prix total en fonction des tickets et de l'offre
        double totalAmount = calculateTotalPrice(tickets, offer);
        order.setTotalAmount(totalAmount);

        // Sauvegarder et retourner la commande
        return saveOrder(order);
    }


    public double calculateTotalPrice(List<TicketDTO> tickets, OfferDTO offer) {
        // Vérifier que la liste des tickets n'est pas vide
        if (tickets == null || tickets.isEmpty()) {
            throw new IllegalArgumentException("La liste des tickets ne peut pas être vide.");
        }

        // Initialiser le prix total
        double totalPrice = 0.0;

        // Calculer le total en fonction du prix de chaque ticket
        for (TicketDTO ticket : tickets) {
            totalPrice += ticket.getPrice();  // Ajouter le prix du ticket au total
        }

        // Ajuster le total en fonction de l'offre sélectionnée
        int numberOfPeople;
        if (offer != null) {
            switch (offer.getName()) {
                case "Solo":
                    numberOfPeople = 1;
                    break;
                case "Duo":
                    numberOfPeople = 2;
                    break;
                case "Familial":
                    numberOfPeople = 4;
                    break;
                default:
                    numberOfPeople = 1; // Valeur par défaut si l'offre est inconnue
                    break;
            }
        } else {
            numberOfPeople = 1;  // Par défaut, un seul billet si l'offre est nulle
        }

        // Multiplier le prix total des tickets par le nombre de personnes dans l'offre
        return totalPrice * numberOfPeople;
    }

    
    public OrderDTO save(OrderDTO orderDTO) {
        // Vérifier que la commande a bien des tickets
        if (orderDTO.getTickets() == null || orderDTO.getTickets().isEmpty()) {
            throw new IllegalArgumentException("La commande doit contenir au moins un ticket.");
        }

        // Convertir le DTO en entité
        Order orderEntity = orderMapper.toEntity(orderDTO);

        // Sauvegarder l'entité dans la base de données
        Order savedOrder = orderRepository.save(orderEntity);

        // Retourner le DTO après sauvegarde
        return orderMapper.toDTO(savedOrder);  // Utiliser toDTO pour renvoyer le DTO mis à jour
    }


}
