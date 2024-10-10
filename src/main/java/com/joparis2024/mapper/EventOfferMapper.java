package com.joparis2024.mapper;

import com.joparis2024.dto.EventOfferDTO;
import com.joparis2024.model.EventOffer;
import com.joparis2024.repository.EventRepository;
import com.joparis2024.repository.OfferRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EventOfferMapper {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private OfferRepository offerRepository;

    public EventOfferDTO toDTO(EventOffer eventOffer) {
        if (eventOffer == null) {
            return null;
        }

        EventOfferDTO dto = new EventOfferDTO();
        dto.setId(eventOffer.getId());
        dto.setEventId(eventOffer.getEvent().getId());  // Utilisation de l'ID de l'événement
        dto.setOfferId(eventOffer.getOffer().getId());  // Utilisation de l'ID de l'offre
        return dto;
    }

    public EventOffer toEntity(EventOfferDTO dto) throws Exception {
        if (dto == null) {
            return null;
        }

        EventOffer eventOffer = new EventOffer();
        eventOffer.setId(dto.getId());
        eventOffer.setEvent(eventRepository.findById(dto.getEventId())
            .orElseThrow(() -> new Exception("Événement non trouvé")));
        eventOffer.setOffer(offerRepository.findById(dto.getOfferId())
            .orElseThrow(() -> new Exception("Offre non trouvée")));
        return eventOffer;
    }
    
    public List<EventOfferDTO> toDTOs(List<EventOffer> eventOffers) {
        if (eventOffers == null || eventOffers.isEmpty()) {
            return new ArrayList<>();
        }

        List<EventOfferDTO> eventOfferDTOs = new ArrayList<>();
        for (EventOffer eventOffer : eventOffers) {
            eventOfferDTOs.add(toDTO(eventOffer));
        }

        return eventOfferDTOs;
    }

    public List<EventOffer> toEntities(List<EventOfferDTO> eventOfferDTOs) throws Exception {
        if (eventOfferDTOs == null || eventOfferDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<EventOffer> eventOffers = new ArrayList<>();
        for (EventOfferDTO eventOfferDTO : eventOfferDTOs) {
            eventOffers.add(toEntity(eventOfferDTO));
        }

        return eventOffers;
    }
}
