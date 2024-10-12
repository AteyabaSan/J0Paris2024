package com.joparis2024.service;

import com.joparis2024.dto.EventOfferDTO;
import com.joparis2024.mapper.EventOfferMapper;
import com.joparis2024.model.EventOffer;
import com.joparis2024.repository.EventOfferRepository;
import com.joparis2024.repository.EventRepository;
import com.joparis2024.repository.OfferRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventOfferService {

    @Autowired
    private EventOfferRepository eventOfferRepository;

    @Autowired
    private EventOfferMapper eventOfferMapper;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private OfferRepository offerRepository;
    

    public EventOfferDTO createEventOffer(EventOfferDTO eventOfferDTO) throws Exception {
        if (eventOfferDTO.getEventId() == null || eventOfferDTO.getOfferId() == null) {
            throw new IllegalArgumentException("Les IDs de l'événement et de l'offre doivent être spécifiés");
        }

        // Vérification supplémentaire pour s'assurer que l'événement et l'offre existent
        if (!eventRepository.existsById(eventOfferDTO.getEventId())) {
            throw new IllegalArgumentException("Événement non trouvé avec ID : " + eventOfferDTO.getEventId());
        }
        if (!offerRepository.existsById(eventOfferDTO.getOfferId())) {
            throw new IllegalArgumentException("Offre non trouvée avec ID : " + eventOfferDTO.getOfferId());
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
