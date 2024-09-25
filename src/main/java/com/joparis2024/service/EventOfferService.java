package com.joparis2024.service;

import com.joparis2024.dto.EventOfferDTO;
import com.joparis2024.model.EventOffer;
import com.joparis2024.repository.EventOfferRepository;
import com.joparis2024.model.Event;
import com.joparis2024.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class EventOfferService {

    @Autowired
    private EventOfferRepository eventOfferRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private OfferService offerService;

    // Créer un EventOffer
    public EventOffer createEventOffer(EventOfferDTO eventOfferDTO) throws Exception {
        EventOffer eventOffer = new EventOffer();
        eventOffer.setEvent(eventService.mapToEntity(eventOfferDTO.getEvent()));  // Mapper Event
        eventOffer.setOffer(offerService.mapToEntity(eventOfferDTO.getOffer()));  // Mapper Offer
        return eventOfferRepository.save(eventOffer);
    }

    // Récupérer toutes les associations EventOffer
    public List<EventOfferDTO> getAllEventOffers() {
        List<EventOffer> eventOffers = eventOfferRepository.findAll();
        List<EventOfferDTO> eventOfferDTOs = new ArrayList<>();
        for (EventOffer eventOffer : eventOffers) {
            eventOfferDTOs.add(mapToDTO(eventOffer));
        }
        return eventOfferDTOs;
    }

    // Supprimer une association EventOffer
    public void deleteEventOffer(Long id) throws Exception {
        Optional<EventOffer> eventOffer = eventOfferRepository.findById(id);
        if (!eventOffer.isPresent()) {
            throw new Exception("L'association Event-Offer n'existe pas");
        }
        eventOfferRepository.delete(eventOffer.get());
    }

    // Mapper EventOffer -> EventOfferDTO
    public EventOfferDTO mapToDTO(EventOffer eventOffer) throws Exception {
        EventOfferDTO dto = new EventOfferDTO();
        dto.setId(eventOffer.getId());
        dto.setEvent(eventService.mapToDTO(eventOffer.getEvent()));
        dto.setOffer(offerService.mapToDTO(eventOffer.getOffer()));
        return dto;
    }

    // Mapper EventOfferDTO -> EventOffer
    public EventOffer mapToEntity(EventOfferDTO dto) throws Exception {
        EventOffer eventOffer = new EventOffer();
        eventOffer.setId(dto.getId());
        eventOffer.setEvent(eventService.mapToEntity(dto.getEvent()));
        eventOffer.setOffer(offerService.mapToEntity(dto.getOffer()));
        return eventOffer;
    }
}
