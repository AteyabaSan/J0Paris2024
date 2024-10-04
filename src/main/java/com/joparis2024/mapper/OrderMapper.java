package com.joparis2024.mapper;

import com.joparis2024.dto.OrderDTO;
import com.joparis2024.dto.TicketDTO;
import com.joparis2024.model.Order;
import com.joparis2024.model.Order_Ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    public OrderDTO toDTO(Order order) throws Exception {
        if (order == null) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUser(userMapper.toDTO(order.getUser())); // Utilisation du mapper User
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setOrderDate(order.getOrderDate());

        // Mapper les tickets
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Order_Ticket orderTicket : order.getOrderTickets()) {
            ticketDTOs.add(ticketMapper.toDTO(orderTicket.getTicket())); // Utilisation du mapper Ticket
        }
        orderDTO.setTickets(ticketDTOs);

        orderDTO.setPayment(paymentMapper.toDTO(order.getPayment())); // Utilisation du mapper Payment

        return orderDTO;
    }

    public Order toEntity(OrderDTO orderDTO) throws Exception {
        if (orderDTO == null) {
            return null;
        }

        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setStatus(orderDTO.getStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setUser(userMapper.toEntity(orderDTO.getUser())); // Utilisation du mapper User

        // Mapper les tickets
        List<Order_Ticket> orderTickets = new ArrayList<>();
        for (TicketDTO ticketDTO : orderDTO.getTickets()) {
            Order_Ticket orderTicket = new Order_Ticket();
            orderTicket.setTicket(ticketMapper.toEntity(ticketDTO)); // Utilisation du mapper Ticket
            orderTickets.add(orderTicket);
        }
        order.setOrderTickets(orderTickets);

        order.setPayment(paymentMapper.toEntity(orderDTO.getPayment())); // Utilisation du mapper Payment

        return order;
    }

    public List<OrderDTO> toDTOs(List<Order> orders) throws Exception {
        if (orders == null || orders.isEmpty()) {
            return new ArrayList<>();
        }

        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(toDTO(order));
        }

        return orderDTOs;
    }

    public List<Order> toEntities(List<OrderDTO> orderDTOs) throws Exception {
        if (orderDTOs == null || orderDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Order> orders = new ArrayList<>();
        for (OrderDTO orderDTO : orderDTOs) {
            orders.add(toEntity(orderDTO));
        }

        return orders;
    }
}
