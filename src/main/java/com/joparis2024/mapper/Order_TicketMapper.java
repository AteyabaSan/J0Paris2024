package com.joparis2024.mapper;

import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.model.Order;
import com.joparis2024.model.Order_Ticket;
import com.joparis2024.model.Ticket;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Order_TicketMapper {

    public Order_TicketDTO toDTO(Order_Ticket orderTicket) {
        if (orderTicket == null) {
            return null;
        }

        return new Order_TicketDTO(
            orderTicket.getId(),
            orderTicket.getOrder().getId(),
            orderTicket.getTicket().getId(),
            orderTicket.getQuantity()
        );
    }

    public Order_Ticket toEntity(Order_TicketDTO orderTicketDTO) {
        if (orderTicketDTO == null) {
            return null;
        }

        Order_Ticket orderTicket = new Order_Ticket();
        orderTicket.setId(orderTicketDTO.getId());
        orderTicket.setQuantity(orderTicketDTO.getQuantity());
        orderTicket.setOrder(new Order(orderTicketDTO.getOrderId())); // Associer avec l'ID de commande
        orderTicket.setTicket(new Ticket(orderTicketDTO.getTicketId())); // Associer avec l'ID de ticket

        return orderTicket;
    }

    public List<Order_TicketDTO> toDTOs(List<Order_Ticket> orderTickets) {
        if (orderTickets == null || orderTickets.isEmpty()) {
            return new ArrayList<>();
        }

        List<Order_TicketDTO> orderTicketDTOs = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les entités en DTOs
        for (Order_Ticket orderTicket : orderTickets) {
            orderTicketDTOs.add(toDTO(orderTicket));
        }

        return orderTicketDTOs;
    }

    public List<Order_Ticket> toEntities(List<Order_TicketDTO> orderTicketDTOs) {
        if (orderTicketDTOs == null || orderTicketDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Order_Ticket> orderTickets = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les DTOs en entités
        for (Order_TicketDTO orderTicketDTO : orderTicketDTOs) {
            orderTickets.add(toEntity(orderTicketDTO));
        }

        return orderTickets;
    }
}
