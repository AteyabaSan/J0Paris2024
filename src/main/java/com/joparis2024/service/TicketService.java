package com.joparis2024.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;



@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketMapper ticketMapper;
    
    @Autowired 
    private TicketRepository ticketRepository;

    @Transactional
    public TicketDTO createTicket(TicketDTO ticketDTO) throws Exception {
        if (ticketDTO.getQuantity() == null || ticketDTO.getQuantity() <= 0) {
            throw new Exception("La quantité doit être supérieure à 0.");
        }
        logger.info("Tentative de création d'un ticket avec la quantité: {}", ticketDTO.getQuantity());

        // S'assurer que l'attribut 'available' est bien pris en compte
        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        Ticket savedTicket = ticketRepository.save(ticket);

        TicketDTO savedTicketDTO = ticketMapper.toDTO(savedTicket);
        logger.info("Ticket créé avec succès. Disponibilité: {}", savedTicketDTO.isAvailable());
        return savedTicketDTO;
    }

    @Transactional
    public TicketDTO updateTicket(Long ticketId, TicketDTO ticketDTO) throws Exception {
        logger.info("Mise à jour du ticket avec ID: {}", ticketId);
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trouvé"));

        // Validation de la quantité
        if (ticketDTO.getQuantity() == null || ticketDTO.getQuantity() <= 0) {
            throw new Exception("La quantité doit être supérieure à 0.");
        }

        // Mise à jour des informations du ticket, y compris la disponibilité
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setQuantity(ticketDTO.getQuantity());
        ticket.setAvailable(ticketDTO.isAvailable());  // Prise en compte de 'available'

        Ticket updatedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDTO(updatedTicket);
    }
    
    @Transactional(readOnly = true)
    public TicketDTO getTicketById(Long ticketId) throws Exception {
        logger.info("Récupération du ticket ID: {}", ticketId);
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trouvé"));

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

    // Nouvelle méthode pour générer un QR code pour un ticket
    public byte[] generateQRCode(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream.toByteArray();
    }
}