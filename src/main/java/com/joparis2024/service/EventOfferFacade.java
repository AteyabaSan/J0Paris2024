package com.joparis2024.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joparis2024.model.Event;
import com.joparis2024.model.EventOffer;
import com.joparis2024.model.Offer;
import com.joparis2024.repository.EventOfferRepository;

@Service
public class EventOfferFacade {

    @Autowired
    private EventOfferRepository eventOfferRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private OfferService offerService;

    // Associer une offre à un événement
    public void assignOfferToEvent(Long eventId, Long offerId) throws Exception {
        // Trouver l'événement et l'offre à associer
        Event event = eventService.findById(eventId);
        Offer offer = offerService.findById(offerId);

        // Créer l'association entre l'événement et l'offre
        EventOffer eventOffer = new EventOffer();
        eventOffer.setEvent(event);
        eventOffer.setOffer(offer);

        // Sauvegarder l'association
        eventOfferRepository.save(eventOffer);
    }

    // Associer plusieurs événements à une offre
    public void assignEventsToOffer(Long offerId, List<Long> eventIds) throws Exception {
        Offer offer = offerService.findById(offerId);

        // Associer chaque événement à l'offre
        for (Long eventId : eventIds) {
            Event event = eventService.findById(eventId);

            EventOffer eventOffer = new EventOffer();
            eventOffer.setEvent(event);
            eventOffer.setOffer(offer);

            // Sauvegarder l'association
            eventOfferRepository.save(eventOffer);
        }
    }

    // Associer plusieurs offres à un événement
    public void assignOffersToEvent(Long eventId, List<Long> offerIds) throws Exception {
        Event event = eventService.findById(eventId);

        // Associer chaque offre à l'événement
        for (Long offerId : offerIds) {
            Offer offer = offerService.findById(offerId);

            EventOffer eventOffer = new EventOffer();
            eventOffer.setEvent(event);
            eventOffer.setOffer(offer);

            // Sauvegarder l'association
            eventOfferRepository.save(eventOffer);
        }
    }

    // Récupérer les offres associées à un événement
    public List<Offer> getOffersForEvent(Long eventId) throws Exception {
        Event event = eventService.findById(eventId);
        List<EventOffer> eventOffers = eventOfferRepository.findByEvent(event);

        // Extraire les offres associées
        List<Offer> offers = new ArrayList<>();
        for (EventOffer eventOffer : eventOffers) {
            offers.add(eventOffer.getOffer());
        }

        return offers;
    }

    // Récupérer les événements associés à une offre
    public List<Event> getEventsForOffer(Long offerId) throws Exception {
        Offer offer = offerService.findById(offerId);
        List<EventOffer> eventOffers = eventOfferRepository.findByOffer(offer);

        // Extraire les événements associés
        List<Event> events = new ArrayList<>();
        for (EventOffer eventOffer : eventOffers) {
            events.add(eventOffer.getEvent());
        }

        return events;
    }
}
