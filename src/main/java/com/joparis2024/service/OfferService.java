package com.joparis2024.service;

import com.joparis2024.dto.OfferDTO;
import com.joparis2024.mapper.OfferMapper;
import com.joparis2024.model.Offer;
import com.joparis2024.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private EventOfferFacade eventOfferFacade;  // Utilisation de la façade pour gérer les événements associés

    // Récupérer toutes les offres
    public List<OfferDTO> getAllOffers() throws Exception {
        List<Offer> offers = offerRepository.findAll();
        List<OfferDTO> offerDTOs = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les entités en DTO
        for (Offer offer : offers) {
            offerDTOs.add(offerMapper.toDTO(offer)); // Peut lever une exception
        }
        return offerDTOs;
    }

    // Créer une offre
    public OfferDTO createOffer(OfferDTO offerDTO) throws Exception {
        Offer offer = offerMapper.toEntity(offerDTO);
        Offer savedOffer = offerRepository.save(offer);

        // Utilisation de la façade pour gérer l'association entre Offer et Event
        if (offerDTO.getEventIds() != null && !offerDTO.getEventIds().isEmpty()) {
            eventOfferFacade.assignEventsToOffer(savedOffer.getId(), offerDTO.getEventIds());
        }

        return offerMapper.toDTO(savedOffer);
    }

    // Récupérer une offre par ID
    public OfferDTO getOfferById(Long id) throws Exception {
        Optional<Offer> offer = offerRepository.findById(id);
        if (offer.isPresent()) {
            return offerMapper.toDTO(offer.get());
        } else {
            throw new Exception("Offer non trouvée");
        }
    }

    // Mettre à jour une offre
    public OfferDTO updateOffer(Long id, OfferDTO offerDTO) throws Exception {
        Optional<Offer> existingOffer = offerRepository.findById(id);
        if (!existingOffer.isPresent()) {
            throw new Exception("Offer non trouvée");
        }

        Offer offer = existingOffer.get();
        offer.setName(offerDTO.getName());
        offer.setNumberOfSeats(offerDTO.getNumberOfSeats());

        Offer updatedOffer = offerRepository.save(offer);

        // Utilisation de la façade pour mettre à jour les événements associés
        if (offerDTO.getEventIds() != null && !offerDTO.getEventIds().isEmpty()) {
            eventOfferFacade.assignEventsToOffer(updatedOffer.getId(), offerDTO.getEventIds());
        }

        return offerMapper.toDTO(updatedOffer);
    }

    // Supprimer une offre
    public void deleteOffer(Long id) throws Exception {
        if (!offerRepository.existsById(id)) {
            throw new Exception("Offer non trouvée");
        }
        offerRepository.deleteById(id);
    }

    // Méthode pour trouver une offre par son ID
    public Offer findById(Long offerId) throws Exception {
        return offerRepository.findById(offerId)
            .orElseThrow(() -> new Exception("Offer non trouvée avec l'ID : " + offerId));
    }
}

