package com.joparis2024.service;

import com.joparis2024.dto.OfferDTO;
import com.joparis2024.mapper.OfferMapper;
import com.joparis2024.model.Offer;
import com.joparis2024.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferMapper offerMapper;

    // Récupérer toutes les offres
    public List<OfferDTO> getAllOffers() throws Exception {
        List<Offer> offers = offerRepository.findAll();
        return offerMapper.toDTOs(offers);
    }

    // Créer une offre
    public OfferDTO createOffer(OfferDTO offerDTO) throws Exception {
        Offer offer = offerMapper.toEntity(offerDTO);
        Offer savedOffer = offerRepository.save(offer);
        return offerMapper.toDTO(savedOffer);
    }

    // Récupérer une offre par ID
    public OfferDTO getOfferById(Long id) throws Exception {
        Offer offer = offerRepository.findById(id)
            .orElseThrow(() -> new Exception("Offer non trouvée"));
        return offerMapper.toDTO(offer);
    }

    // Mettre à jour une offre
    public OfferDTO updateOffer(Long id, OfferDTO offerDTO) throws Exception {
        Offer existingOffer = offerRepository.findById(id)
            .orElseThrow(() -> new Exception("Offer non trouvée"));

        existingOffer.setName(offerDTO.getName());
        existingOffer.setNumberOfSeats(offerDTO.getNumberOfSeats());

        Offer updatedOffer = offerRepository.save(existingOffer);
        return offerMapper.toDTO(updatedOffer);
    }

    // Supprimer une offre
    public void deleteOffer(Long id) throws Exception {
        if (!offerRepository.existsById(id)) {
            throw new Exception("Offer non trouvée");
        }
        offerRepository.deleteById(id);
    }

    // Trouver une offre par ID (utilisation interne)
    public Offer findById(Long offerId) throws Exception {
        return offerRepository.findById(offerId)
            .orElseThrow(() -> new Exception("Offer non trouvée"));
    }
}
