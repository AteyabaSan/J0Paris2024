package com.joparis2024.controller;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            // Log de vérification du contenu de OrderDTO reçu
            logger.info("OrderDTO reçu: {}", orderDTO);

            // Vérification si le DTO est bien présent
            if (orderDTO == null) {
                throw new IllegalArgumentException("Le DTO de commande est nul.");
            }

            // Ajout du log avant création
            logger.info("Contenu de OrderDTO avant création : {}", orderDTO);

            // Création de la commande via le service
            OrderDTO createdOrder = orderService.createOrder(orderDTO);

            // Log de confirmation
            logger.info("Commande créée avec succès: {}", createdOrder.getId());

            // Retourner la réponse avec statut 201
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            // Log de l'erreur si le DTO est incorrect
            logger.error("Erreur: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log détaillé en cas d'erreur
            logger.error("Erreur lors de la création de la commande : ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Récupérer toutes les commandes (READ)
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        try {
            logger.info("Récupération de toutes les commandes.");
            List<OrderDTO> orders = orderService.getAllOrders();
            logger.info("Nombre de commandes récupérées : {}", orders.size());
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des commandes : ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Récupérer une commande par ID (READ)
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        try {
            logger.info("Recherche de la commande avec ID : {}", id);
            OrderDTO order = orderService.getOrderById(id);
            logger.info("Commande trouvée : {}", order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de la commande : ", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Mettre à jour une commande (UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        try {
            logger.info("Mise à jour de la commande avec ID : {}, nouvelles infos : {}", id, orderDTO);
            OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
            logger.info("Commande mise à jour avec succès : {}", updatedOrder);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la commande : ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Supprimer une commande (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            logger.info("Suppression de la commande avec ID : {}", id);
            orderService.cancelOrder(id);
            logger.info("Commande supprimée avec succès.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la commande : ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
