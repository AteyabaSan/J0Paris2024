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

        // Mapping manuel des événements associés en utilisant uniquement leurs IDs
        List<Long> eventIds = new ArrayList<>();
        if (offer.getEvents() != null) {
            for (Event event : offer.getEvents()) {
                eventIds.add(event.getId());  // Utilisation des IDs des événements
            }
        }
        offerDTO.setEventIds(eventIds);

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

        // Remarque : Les relations avec les événements seront gérées dans le service
        return offer;
    }
}
