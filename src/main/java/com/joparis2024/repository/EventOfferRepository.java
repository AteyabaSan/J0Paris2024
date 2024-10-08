package com.joparis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.EventOffer;
import com.joparis2024.model.Event;
import com.joparis2024.model.Offer;
import java.util.List;
import java.util.Optional;

public interface EventOfferRepository extends JpaRepository<EventOffer, Long> {
    List<EventOffer> findByEvent(Event event);
    List<EventOffer> findByOffer(Offer offer);
 // Méthode personnalisée pour trouver un EventOffer par EventId et OfferId
    Optional<EventOffer> findByEventIdAndOfferId(Long eventId, Long offerId);
}
