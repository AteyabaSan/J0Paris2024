package com.joparis2024.service;

import com.joparis2024.dto.OfferDTO;
import com.joparis2024.model.Offer;
import com.joparis2024.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private EventService eventService;

    // Récupérer toutes les offres
    public List<OfferDTO> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Créer une offre
    public OfferDTO createOffer(OfferDTO offerDTO) throws Exception {
        Offer offer = mapToEntity(offerDTO);
        Offer savedOffer = offerRepository.save(offer);
        return mapToDTO(savedOffer);
    }

    // Récupérer une offre par ID
    public OfferDTO getOfferById(Long id) throws Exception {
        Optional<Offer> offer = offerRepository.findById(id);
        if (offer.isPresent()) {
            return mapToDTO(offer.get());
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
        offer.setEvents(eventService.mapToEntities(offerDTO.getEvents()));

        Offer updatedOffer = offerRepository.save(offer);
        return mapToDTO(updatedOffer);
    }

    // Supprimer une offre
    public void deleteOffer(Long id) throws Exception {
        if (!offerRepository.existsById(id)) {
            throw new Exception("Offer non trouvée");
        }
        offerRepository.deleteById(id);
    }

    // Mapper Offer -> OfferDTO
    public OfferDTO mapToDTO(Offer offer) {
        try {
            return new OfferDTO(
                offer.getId(),
                offer.getName(),
                offer.getNumberOfSeats(),
                eventService.mapToDTOs(offer.getEvents())  // Peut lever une exception
            );
        } catch (Exception e) {
            // Gestion de l'erreur ici, peut-être en lançant une RuntimeException ou en renvoyant une valeur par défaut
            throw new RuntimeException("Erreur lors du mapping des événements dans l'offre", e);
        }
    }


    // Mapper OfferDTO -> Offer
    public Offer mapToEntity(OfferDTO offerDTO) throws Exception {
        Offer offer = new Offer();
        offer.setId(offerDTO.getId());
        offer.setName(offerDTO.getName());
        offer.setNumberOfSeats(offerDTO.getNumberOfSeats());
        offer.setEvents(eventService.mapToEntities(offerDTO.getEvents()));
        return offer;
    }
}
