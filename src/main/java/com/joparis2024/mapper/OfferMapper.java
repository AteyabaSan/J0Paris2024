package com.joparis2024.mapper;

import org.springframework.stereotype.Component;

import com.joparis2024.dto.OfferDTO;
import com.joparis2024.model.Event;
import com.joparis2024.model.Offer;
import java.util.ArrayList;
import java.util.List;

@Component
public class OfferMapper {

    public OfferDTO toDTO(Offer offer) {
        if (offer == null) {
            return null;
        }

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setName(offer.getName());
        offerDTO.setNumberOfSeats(offer.getNumberOfSeats());

        // Optionnel : ne pas associer d'événements si non présents
        if (offer.getEvents() != null && !offer.getEvents().isEmpty()) {
            List<Long> eventIds = new ArrayList<>();
            for (Event event : offer.getEvents()) {
                if (event != null) {
                    eventIds.add(event.getId());  // Utilisation des IDs des événements
                }
            }
            offerDTO.setEventIds(eventIds);
        }

        return offerDTO;
    }

    public Offer toEntity(OfferDTO offerDTO) {
        if (offerDTO == null) {
            return null;
        }

        Offer offer = new Offer();
        offer.setId(offerDTO.getId());
        offer.setName(offerDTO.getName());
        offer.setNumberOfSeats(offerDTO.getNumberOfSeats());

        // Remarque : Ne pas gérer les événements ici, cela se fait dans la facade
        return offer;
    }

    // Méthode pour convertir une liste d'offres en DTOs
    public List<OfferDTO> toDTOs(List<Offer> offers) {
        if (offers == null || offers.isEmpty()) {
            return new ArrayList<>();
        }

        List<OfferDTO> offerDTOs = new ArrayList<>();
        for (Offer offer : offers) {
            offerDTOs.add(toDTO(offer));  // Conversion de chaque Offer en OfferDTO
        }
        return offerDTOs;
    }
}
