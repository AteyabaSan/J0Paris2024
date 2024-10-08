package com.joparis2024.mapper;

import com.joparis2024.dto.EventOfferDTO;
import com.joparis2024.model.EventOffer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EventOfferMapper {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private OfferMapper offerMapper;

    public EventOfferDTO toDTO(EventOffer eventOffer) throws Exception {
        if (eventOffer == null) {
            return null;
        }

        EventOfferDTO dto = new EventOfferDTO();
        dto.setId(eventOffer.getId());
        dto.setEvent(eventMapper.toDTO(eventOffer.getEvent()));  // Utilisation correcte de EventMapper
        dto.setOffer(offerMapper.toDTO(eventOffer.getOffer()));  // Utilisation correcte de OfferMapper
        return dto;
    }

    public EventOffer toEntity(EventOfferDTO dto) throws Exception {
        if (dto == null) {
            return null;
        }

        EventOffer eventOffer = new EventOffer();
        eventOffer.setId(dto.getId());
        eventOffer.setEvent(eventMapper.toEntity(dto.getEvent()));  // Utilisation correcte de EventMapper
        eventOffer.setOffer(offerMapper.toEntity(dto.getOffer()));  // Utilisation correcte de OfferMapper
        return eventOffer;
    }
    
    // Nouvelle méthode pour convertir une liste d'EventOffer en une liste d'EventOfferDTO
    public List<EventOfferDTO> toDTOs(List<EventOffer> eventOffers) throws Exception {
        if (eventOffers == null || eventOffers.isEmpty()) {
            return new ArrayList<>();
        }

        List<EventOfferDTO> eventOfferDTOs = new ArrayList<>();
        for (EventOffer eventOffer : eventOffers) {
            eventOfferDTOs.add(toDTO(eventOffer));
        }

        return eventOfferDTOs;
    }

    // Optionnel : si tu as besoin de convertir une liste de DTOs en entités
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
