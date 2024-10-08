package com.joparis2024.service;

import com.joparis2024.dto.TicketDTO;
import com.joparis2024.mapper.TicketMapper;
import com.joparis2024.model.Ticket;
import com.joparis2024.repository.TicketRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketMapper ticketMapper;
    
    @Autowired TicketRepository ticketRepository;

    @Transactional
    public TicketDTO createTicket(TicketDTO ticketDTO) throws Exception {
        logger.info("Tentative de création d'un ticket : {}", ticketDTO);

        // Création et sauvegarde du ticket
        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDTO(savedTicket);
    }

    @Transactional
    public TicketDTO updateTicket(Long ticketId, TicketDTO ticketDTO) throws Exception {
        logger.info("Mise à jour du ticket avec ID: {}", ticketId);
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trouvé"));

        // Mise à jour des informations du ticket
        ticket.setPrice(ticketDTO.getPrice());  // Mise à jour du prix uniquement
        Ticket updatedTicket = ticketRepository.save(ticket);

        return ticketMapper.toDTO(updatedTicket);  // Conversion en DTO et retour
    }


    @Transactional(readOnly = true)
    public TicketDTO getTicketById(Long ticketId) throws Exception {
        logger.info("Récupération du ticket ID: {}", ticketId);
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trouvé"));

        // Utilise le TicketMapper pour convertir Ticket en TicketDTO
        return ticketMapper.toDTO(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> getAllTickets() throws Exception {
        logger.info("Récupération de tous les tickets");
        List<Ticket> tickets = ticketRepository.findAll();
        return ticketMapper.toDTOs(tickets);
    }

    @Transactional
    public void deleteTicket(Long ticketId) throws Exception {
        logger.info("Suppression du ticket avec ID: {}", ticketId);
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trouvé"));
        ticketRepository.delete(ticket);
    }

    // Méthode pour convertir une entité Ticket en DTO
    public TicketDTO convertToDTO(Ticket ticket) {
        return ticketMapper.toDTO(ticket);  // Centralisation de la conversion dans le service
    }
}
