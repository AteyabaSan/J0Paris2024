package com.joparis2024.mapper;

import com.joparis2024.dto.Order_TicketDTO;
import com.joparis2024.model.Offer;
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
        dto.setOfferId(orderTicket.getOffer().getId()); // Inclure l'Offer dans le DTO
        return dto;
    }

    public Order_Ticket toEntity(Order_TicketDTO dto) {
        if (dto == null) {
            return null;
        }
        Order_Ticket orderTicket = new Order_Ticket();
        orderTicket.setId(dto.getId());
        orderTicket.setQuantity(dto.getQuantity());

        // Gestion des relations, on peut récupérer l'Offer ici
        Offer offer = new Offer();
        offer.setId(dto.getOfferId());
        orderTicket.setOffer(offer);  // Assigner l'Offer à l'entité

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
        for (Order_TicketDTO orderTicketDTO : orderTicketDTOs) {
            try {
                Order_Ticket orderTicket = toEntity(orderTicketDTO);
                orderTickets.add(orderTicket);
            } catch (Exception e) {
                // Gérer l'exception ou la loguer
            }
        }

        return orderTickets;
    }
}
