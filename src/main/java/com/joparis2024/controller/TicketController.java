package com.joparis2024.controller;

//import com.joparis2024.dto.TicketDTO;
//import com.joparis2024.mapper.TicketMapper;
//import com.joparis2024.model.Ticket;
//import com.joparis2024.service.TicketService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//
//import javax.validation.Valid;
//
//@RestController
//@RequestMapping("/api/tickets")
public class TicketController {

//    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
//
//    @Autowired
//    private TicketService ticketService;
//
//    @Autowired
//    private TicketMapper ticketMapper; // Injection du mapper
//
//    // Créer un ticket (CREATE)
//    @PostMapping
//    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketDTO ticketDTO) {
//        try {
//            logger.info("Tentative de création d'un ticket : {}", ticketDTO);
//
//            Ticket createdTicket = ticketService.createTicket(ticketDTO);
//
//            // Mapper le ticket créé en DTO et le renvoyer
//            TicketDTO createdTicketDTO = ticketMapper.toDTO(createdTicket);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdTicketDTO);
//
//        } catch (Exception e) {
//            logger.error("Erreur lors de la création du ticket : {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
//
//    // Récupérer un ticket par ID (READ)
//    @GetMapping("/{id}")
//    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
//        try {
//            logger.info("Recherche du ticket avec ID : {}", id);
//            
//            // Récupération du ticket et mappage en DTO
//            TicketDTO ticketDTO = ticketService.getTicketById(id);
//            
//            logger.info("Ticket trouvé : {}", ticketDTO);
//            return ResponseEntity.ok(ticketDTO);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la recherche du ticket : {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
//
//    // Récupérer tous les tickets (READ)
//    @GetMapping
//    public ResponseEntity<List<TicketDTO>> getAllTickets() {
//        try {
//            logger.info("Récupération de tous les tickets.");
//            
//            // Récupération des tickets et mappage en DTO
//            List<TicketDTO> ticketDTOs = ticketService.getAllTickets();
//            
//            logger.info("Tickets récupérés : {}", ticketDTOs);
//            return ResponseEntity.ok(ticketDTOs);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération des tickets : {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
//
//    // Mettre à jour un ticket (UPDATE)
//    @PutMapping("/{id}")
//    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @Valid @RequestBody TicketDTO ticketDTO) {
//        try {
//            logger.info("Mise à jour du ticket avec ID : {}, nouvelles informations : {}", id, ticketDTO);
//            
//            // Mise à jour du ticket et mappage en DTO
//            Ticket updatedTicket = ticketService.updateTicket(id, ticketDTO);
//            TicketDTO updatedTicketDTO = ticketMapper.toDTO(updatedTicket);
//            
//            logger.info("Ticket mis à jour avec succès : {}", updatedTicketDTO);
//            return ResponseEntity.ok(updatedTicketDTO);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la mise à jour du ticket : {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
//
//    // Supprimer un ticket (DELETE)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
//        try {
//            logger.info("Suppression du ticket avec ID : {}", id);
//            ticketService.deleteTicket(id);
//            logger.info("Ticket supprimé avec succès.");
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            logger.error("Erreur lors de la suppression du ticket : {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
}
