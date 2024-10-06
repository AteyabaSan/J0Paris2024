package com.joparis2024.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joparis2024.dto.EventDTO;
import com.joparis2024.dto.OfferDTO;
import com.joparis2024.model.Event;
import com.joparis2024.model.Offer;
import java.util.ArrayList;
import java.util.List;

@Component
public class OfferMapper {

    @Autowired
    private EventMapper eventMapper;  // Injection du EventMapper

    public OfferDTO toDTO(Offer offer) throws Exception {
        if (offer == null) {
            return null;
        }

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setName(offer.getName());
        offerDTO.setNumberOfSeats(offer.getNumberOfSeats());

        // Mapping manuel des événements associés
        List<EventDTO> eventDTOs = new ArrayList<>();
        if (offer.getEvents() != null) {
            for (Event event : offer.getEvents()) {
                eventDTOs.add(eventMapper.toDTO(event)); // Gérer l'exception ici
            }
        }
        offerDTO.setEvents(eventDTOs);

        return offerDTO;
    }

    public Offer toEntity(OfferDTO offerDTO) throws Exception {
        if (offerDTO == null) {
            return null;
        }

        Offer offer = new Offer();
        offer.setId(offerDTO.getId());
        offer.setName(offerDTO.getName());
        offer.setNumberOfSeats(offerDTO.getNumberOfSeats());

        // Mapping manuel des événements associés
        List<Event> events = new ArrayList<>();
        if (offerDTO.getEvents() != null) {
            for (EventDTO eventDTO : offerDTO.getEvents()) {
                events.add(eventMapper.toEntity(eventDTO)); // Gérer l'exception ici
            }
        }
        offer.setEvents(events);

        return offer;
    }
}
