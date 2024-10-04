package com.joparis2024.service;

import com.joparis2024.dto.EventOfferDTO;
import com.joparis2024.mapper.EventOfferMapper;
import com.joparis2024.model.EventOffer;
import com.joparis2024.repository.EventOfferRepository;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventOfferService {

    @Autowired
    private EventOfferRepository eventOfferRepository;

    @Autowired
    private EventOfferMapper eventOfferMapper;

    // Créer un EventOffer
    public EventOfferDTO createEventOffer(EventOfferDTO eventOfferDTO) throws Exception {
        if (eventOfferDTO.getEvent() == null || eventOfferDTO.getOffer() == null) {
            throw new IllegalArgumentException("L'événement et l'offre doivent être spécifiés");
        }
        // Utilisation du mappage pour convertir DTO en entité
        EventOffer eventOffer = eventOfferMapper.toEntity(eventOfferDTO);
        return eventOfferMapper.toDTO(eventOfferRepository.save(eventOffer)); // Renvoi du DTO après sauvegarde
    }

    // Récupérer toutes les associations EventOffer
    public List<EventOfferDTO> getAllEventOffers() {
        List<EventOffer> eventOffers = eventOfferRepository.findAll();
        List<EventOfferDTO> eventOfferDTOs = new ArrayList<>();
        // Gestion des erreurs avec try-catch lors du mapping
        for (EventOffer eventOffer : eventOffers) {
            try {
                eventOfferDTOs.add(eventOfferMapper.toDTO(eventOffer));
            } catch (Exception e) {
                // Log de l'erreur en cas de problème
                LoggerFactory.getLogger(EventOfferService.class).error("Erreur lors du mapping de l'EventOffer", e);
            }
        }
        return eventOfferDTOs; // Retour de la liste des DTOs
    }

    // Supprimer une association EventOffer
    public void deleteEventOffer(Long id) throws Exception {
        Optional<EventOffer> eventOffer = eventOfferRepository.findById(id);
        if (!eventOffer.isPresent()) {
            throw new Exception("L'association Event-Offer n'existe pas");
        }
        eventOfferRepository.delete(eventOffer.get());
    }
}
