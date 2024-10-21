package com.joparis2024.controller;

import com.joparis2024.dto.OfferDTO;
import com.joparis2024.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    // Initialisation du logger
    private static final Logger logger = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    private OfferService offerService;

    // Créer une offre
    @PostMapping
    public ResponseEntity<OfferDTO> createOffer(@RequestBody OfferDTO offerDTO) {
        logger.info("Requête pour créer une nouvelle offre avec le nom : {}", offerDTO.getName());
        try {
            OfferDTO savedOffer = offerService.createOffer(offerDTO);
            logger.info("Offre créée avec succès : {}", savedOffer.getId());
            return new ResponseEntity<>(savedOffer, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'offre : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer une offre par ID
    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long id) {
        logger.info("Requête pour récupérer l'offre avec l'ID : {}", id);
        try {
            OfferDTO offer = offerService.getOfferById(id);
            logger.info("Offre trouvée avec succès pour l'ID : {}", id);
            return new ResponseEntity<>(offer, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'offre pour l'ID {} : {}", id, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer toutes les offres
    @GetMapping
    public ResponseEntity<List<OfferDTO>> getAllOffers() {
        logger.info("Requête pour récupérer toutes les offres");
        try {
            List<OfferDTO> offers = offerService.getAllOffers();
            logger.info("Nombre d'offres récupérées : {}", offers.size());
            return new ResponseEntity<>(offers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des offres : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Mettre à jour une offre
    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable Long id, @RequestBody OfferDTO offerDTO) {
        logger.info("Requête pour mettre à jour l'offre avec l'ID : {}", id);
        try {
            OfferDTO updatedOffer = offerService.updateOffer(id, offerDTO);
            logger.info("Offre mise à jour avec succès pour l'ID : {}", id);
            return new ResponseEntity<>(updatedOffer, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'offre pour l'ID {} : {}", id, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer une offre
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteOffer(@PathVariable Long id) {
        logger.info("Requête pour supprimer l'offre avec l'ID : {}", id);
        try {
            offerService.deleteOffer(id);
            logger.info("Offre supprimée avec succès pour l'ID : {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'offre pour l'ID {} : {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
//    @GetMapping("/event/{eventId}")
//    public String getOffersByEvent(@PathVariable Long eventId, Model model) {
//        List<OfferDTO> offers = offerService.getOffersByEvent(eventId);
//        model.addAttribute("offers", offers);
//        return "offers"; // Page HTML des offres pour cet événement
//    }
}