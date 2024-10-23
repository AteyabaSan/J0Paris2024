package com.joparis2024.controller;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.service.OrderService;
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

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Créer une commande
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        logger.info("Création d'une nouvelle commande");
        try {
            OrderDTO createdOrder = orderService.createOrder(orderDTO);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la commande : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer toutes les commandes
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        logger.info("Récupération de toutes les commandes");
        try {
            List<OrderDTO> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des commandes : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer une commande par ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        logger.info("Récupération de la commande avec ID: {}", id);
        try {
            OrderDTO orderDTO = orderService.getOrderById(id);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la commande avec ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Mettre à jour une commande
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        logger.info("Mise à jour de la commande avec ID: {}", id);
        try {
            OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la commande avec ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        logger.info("Requête reçue pour supprimer la commande avec ID: {}", id);
        try {
            orderService.cancelOrder(id);  // Appelle la méthode de service pour annuler la commande
            logger.info("Commande supprimée avec succès, ID : {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la commande : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
