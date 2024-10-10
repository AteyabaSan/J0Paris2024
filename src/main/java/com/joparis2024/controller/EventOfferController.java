package com.joparis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.joparis2024.dto.EventOfferDTO;

import com.joparis2024.service.EventOfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/event-offers")
public class EventOfferController {

    @Autowired
    private EventOfferService eventOfferService;

    private static final Logger logger = LoggerFactory.getLogger(EventOfferController.class);

    // Créer une association EventOffer
    @PostMapping
    public ResponseEntity<EventOfferDTO> createEventOffer(@RequestBody EventOfferDTO eventOfferDTO) {
        try {
            logger.info("Requête pour créer une association Event-Offer");
            EventOfferDTO createdEventOffer = eventOfferService.createEventOffer(eventOfferDTO);
            return new ResponseEntity<>(createdEventOffer, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de la création de l'association Event-Offer : " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création de l'association Event-Offer", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer toutes les associations EventOffer
    @GetMapping
    public ResponseEntity<List<EventOfferDTO>> getAllEventOffers() {
        try {
            logger.info("Requête pour récupérer toutes les associations Event-Offer");
            List<EventOfferDTO> eventOfferDTOs = eventOfferService.getAllEventOffers();
            return new ResponseEntity<>(eventOfferDTOs, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des associations Event-Offer", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer une association EventOffer par son ID
    @GetMapping("/{id}")
    public ResponseEntity<EventOfferDTO> getEventOfferById(@PathVariable Long id) {
        try {
            logger.info("Requête pour récupérer l'association Event-Offer avec ID : " + id);
            EventOfferDTO eventOfferDTO = eventOfferService.getEventOfferById(id);
            return new ResponseEntity<>(eventOfferDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'association Event-Offer avec ID : " + id, e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer une association EventOffer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventOffer(@PathVariable Long id) {
        try {
            logger.info("Requête pour supprimer l'association Event-Offer avec ID : " + id);
            eventOfferService.deleteEventOffer(id);
            logger.info("Association Event-Offer supprimée avec succès");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'association Event-Offer avec ID : " + id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}