package com.joparis2024.controller;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            // Log de vérification du contenu de OrderDTO reçu
            System.out.println("OrderDTO reçu: " + orderDTO);

            // Vérification si le DTO est bien présent
            if (orderDTO == null) {
                throw new IllegalArgumentException("Le DTO de commande est nul.");
            }

            // Ajoutez le log ici pour voir le contenu de OrderDTO avant le mappage
            System.out.println("Contenu de OrderDTO avant mappage : " + orderDTO);

            // Création de la commande à partir du service
            OrderDTO createdOrder = orderService.mapToDTO(orderService.createOrder(orderDTO));

            // Log de confirmation de la création
            System.out.println("Commande créée avec succès: " + createdOrder.getId());

            // Retourner la réponse avec le statut HTTP 201 (Created)
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            // Log de l'exception si le DTO est nul ou un autre argument est incorrect
            System.err.println("Erreur: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log détaillé en cas d'autre exception pour le debug
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // Récupérer toutes les commandes (READ)
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        try {
            List<OrderDTO> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Récupérer une commande par ID (READ)
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        try {
            OrderDTO order = orderService.getOrderById(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Mettre à jour une commande (UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO updatedOrder = orderService.mapToDTO(orderService.updateOrder(id, orderDTO));
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Supprimer une commande (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.cancelOrder(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
