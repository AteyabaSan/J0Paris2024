package com.joparis2024.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.joparis2024.dto.OfferDTO;
import com.joparis2024.model.Offer;
import java.util.ArrayList;
import java.util.List;

@Component
public class OfferMapper {

    @Autowired
    private EventMapper eventMapper; // Injection du EventMapper pour le mappage des événements

    // Mapper Offer -> OfferDTO
    public OfferDTO toDTO(Offer offer) throws Exception {
        if (offer == null) {
            return null;
        }

        return new OfferDTO(
                offer.getId(),
                offer.getName(),
                offer.getNumberOfSeats(),
                eventMapper.toDTOs(offer.getEvents()) // Utiliser EventMapper pour les événements
        );
    }

    // Mapper OfferDTO -> Offer
    public Offer toEntity(OfferDTO offerDTO) throws Exception {
        if (offerDTO == null) {
            return null;
        }

        Offer offer = new Offer();
        offer.setId(offerDTO.getId());
        offer.setName(offerDTO.getName());
        offer.setNumberOfSeats(offerDTO.getNumberOfSeats());
        offer.setEvents(eventMapper.toEntities(offerDTO.getEvents())); // Utiliser EventMapper pour les événements

        return offer;
    }

    // Mapper une liste d'Offer -> une liste d'OfferDTO
    public List<OfferDTO> toDTOs(List<Offer> offers) throws Exception {
        if (offers == null || offers.isEmpty()) {
            return new ArrayList<>();
        }

        List<OfferDTO> offerDTOs = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les entités en DTOs
        for (Offer offer : offers) {
            offerDTOs.add(toDTO(offer));
        }

        return offerDTOs;
    }

    // Mapper une liste d'OfferDTO -> une liste d'Offer
    public List<Offer> toEntities(List<OfferDTO> offerDTOs) throws Exception {
        if (offerDTOs == null || offerDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Offer> offers = new ArrayList<>();
        // Utilisation d'une boucle classique pour transformer les DTOs en entités
        for (OfferDTO offerDTO : offerDTOs) {
            offers.add(toEntity(offerDTO));
        }

        return offers;
    }
}
