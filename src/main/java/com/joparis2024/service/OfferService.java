package com.joparis2024.service;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.OfferDTO;
import com.joparis2024.mapper.EventMapper;
import com.joparis2024.mapper.OfferMapper;
import com.joparis2024.model.Event;
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
    private EventMapper eventMapper;

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

        // Mapping explicite des événements
        if (offerDTO.getEvents() != null && !offerDTO.getEvents().isEmpty()) {
            List<Event> events = new ArrayList<>();
            for (EventDTO eventDTO : offerDTO.getEvents()) {
                events.add(eventMapper.toEntity(eventDTO));  // Mapping manuel des événements
            }
            offer.setEvents(events);
        }

        Offer updatedOffer = offerRepository.save(offer);
        return offerMapper.toDTO(updatedOffer);
    }

    // Supprimer une offre
    public void deleteOffer(Long id) throws Exception {
        if (!offerRepository.existsById(id)) {
            throw new Exception("Offer non trouvée");
        }
        offerRepository.deleteById(id);
    }
}
