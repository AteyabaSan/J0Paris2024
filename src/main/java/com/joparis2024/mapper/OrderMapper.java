package com.joparis2024.mapper;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.OrderSimpleDTO;
import com.joparis2024.model.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PaymentMapper paymentMapper;
    
    // Méthode pour mapper vers OrderSimpleDTO
    public OrderSimpleDTO toSimpleDTO(Order order) {
        if (order == null) {
            return null;
        }
        return new OrderSimpleDTO(order.getId(), order.getStatus(), order.getTotalAmount());
    }

    // Méthode pour mapper de OrderSimpleDTO vers Order (si besoin)
    public Order toEntity(OrderSimpleDTO orderSimpleDTO) {
        if (orderSimpleDTO == null) {
            return null;
        }
        Order order = new Order();
        order.setId(orderSimpleDTO.getId());
        order.setStatus(orderSimpleDTO.getStatus());
        order.setTotalAmount(orderSimpleDTO.getTotalAmount());
        return order;
    }

    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUser(userMapper.toDTO(order.getUser()));  // Utilisation du mapper User
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setOrderDate(order.getOrderDate());

        // Mapping de la relation Payment
        if (order.getPayment() != null) {
            orderDTO.setPayment(paymentMapper.toDTO(order.getPayment()));
        }

        return orderDTO;
    }

    public Order toEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }

        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setStatus(orderDTO.getStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setOrderDate(orderDTO.getOrderDate());

        // Mapping de l'utilisateur via UserMapper avec gestion de l'exception
        if (orderDTO.getUser() != null) {
            try {
                order.setUser(userMapper.toEntity(orderDTO.getUser()));
            } catch (Exception e) {
                // Log de l'erreur et gestion possible de l'exception
                System.err.println("Erreur lors du mapping de l'utilisateur : " + e.getMessage());
                // Vous pouvez relancer l'exception si nécessaire ou renvoyer une valeur par défaut
            }
        }

        // Mapping du paiement
        if (orderDTO.getPayment() != null) {
            order.setPayment(paymentMapper.toEntity(orderDTO.getPayment()));
        }

        return order;
    }
}
