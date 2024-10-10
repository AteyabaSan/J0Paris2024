package com.joparis2024.mapper;

import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.model.Order_Ticket;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class Order_TicketMapper {

    public Order_TicketDTO toDTO(Order_Ticket orderTicket) {
        if (orderTicket == null) {
            return null;
        }
        Order_TicketDTO dto = new Order_TicketDTO();
        dto.setId(orderTicket.getId());
        dto.setOrderId(orderTicket.getOrder().getId());
        dto.setTicketId(orderTicket.getTicket().getId());
        dto.setQuantity(orderTicket.getQuantity());
        return dto;
    }

    public Order_Ticket toEntity(Order_TicketDTO dto) {
        if (dto == null) {
            return null;
        }
        Order_Ticket orderTicket = new Order_Ticket();
        orderTicket.setId(dto.getId());
        // On laisse la gestion des relations à une facade si nécessaire
        orderTicket.setQuantity(dto.getQuantity());
        return orderTicket;
    }

    // Méthode pour convertir une liste de Order_Ticket en DTOs
    public List<Order_TicketDTO> toDTOs(List<Order_Ticket> orderTickets) {
        List<Order_TicketDTO> dtoList = new ArrayList<>();
        for (Order_Ticket orderTicket : orderTickets) {
            dtoList.add(toDTO(orderTicket));
        }
        return dtoList;
    }

    public List<Order_Ticket> toEntities(List<Order_TicketDTO> orderTicketDTOs) {
        if (orderTicketDTOs == null || orderTicketDTOs.isEmpty()) {
           
            return new ArrayList<>();
        }

        List<Order_Ticket> orderTickets = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les DTOs en entités
        for (Order_TicketDTO orderTicketDTO : orderTicketDTOs) {
            try {
                Order_Ticket orderTicket = toEntity(orderTicketDTO); // Conversion d'un DTO en entité
                orderTickets.add(orderTicket);
             
            } catch (Exception e) {
                
            }
        }

        return orderTickets;
    }

}
