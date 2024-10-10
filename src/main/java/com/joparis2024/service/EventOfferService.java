package com.joparis2024.service;

import com.joparis2024.dto.EventOfferDTO;
import com.joparis2024.mapper.EventOfferMapper;
import com.joparis2024.model.EventOffer;
import com.joparis2024.repository.EventOfferRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventOfferService {

    @Autowired
    private EventOfferRepository eventOfferRepository;

    @Autowired
    private EventOfferMapper eventOfferMapper;

    public EventOfferDTO createEventOffer(EventOfferDTO eventOfferDTO) throws Exception {
        if (eventOfferDTO.getEventId() == null || eventOfferDTO.getOfferId() == null) {
            throw new IllegalArgumentException("Les IDs de l'événement et de l'offre doivent être spécifiés");
        }
        EventOffer eventOffer = eventOfferMapper.toEntity(eventOfferDTO);
        return eventOfferMapper.toDTO(eventOfferRepository.save(eventOffer));
    }

    public List<EventOfferDTO> getAllEventOffers() throws Exception {
        List<EventOffer> eventOffers = eventOfferRepository.findAll();
        return eventOfferMapper.toDTOs(eventOffers);
    }

    public void deleteEventOffer(Long id) throws Exception {
        EventOffer eventOffer = eventOfferRepository.findById(id)
            .orElseThrow(() -> new Exception("L'association Event-Offer n'existe pas"));
        eventOfferRepository.delete(eventOffer);
    }
    
    public EventOfferDTO getEventOfferById(Long id) throws Exception {
        EventOffer eventOffer = eventOfferRepository.findById(id)
                .orElseThrow(() -> new Exception("L'association Event-Offer avec ID : " + id + " n'existe pas"));
        return eventOfferMapper.toDTO(eventOffer);
    }

}
