package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.joparis2024.dto.OfferDTO;
import com.joparis2024.mapper.OfferMapper;
import com.joparis2024.model.Offer;
import com.joparis2024.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

    @InjectMocks
    private OfferService offerService;

    private Offer offer;
    private OfferDTO offerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        offer = new Offer();
        offer.setId(1L);
        offer.setName("Test Offer");
        offer.setNumberOfSeats(100);

        offerDTO = new OfferDTO();
        offerDTO.setId(1L);
        offerDTO.setName("Test Offer");
        offerDTO.setNumberOfSeats(100);
    }

    @Test
    void testGetAllOffers_Success() throws Exception {
        // Mock des méthodes du repository et mapper
        List<Offer> offers = Arrays.asList(offer);
        when(offerRepository.findAll()).thenReturn(offers);
        when(offerMapper.toDTOs(offers)).thenReturn(Arrays.asList(offerDTO));

        // Appel du service
        List<OfferDTO> result = offerService.getAllOffers();

        // Vérifications
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Offer", result.get(0).getName());

        verify(offerRepository, times(1)).findAll();
    }

    @Test
    void testCreateOffer_Success() throws Exception {
        // Mock des méthodes du mapper et repository
        when(offerMapper.toEntity(offerDTO)).thenReturn(offer);
        when(offerRepository.save(offer)).thenReturn(offer);
        when(offerMapper.toDTO(offer)).thenReturn(offerDTO);

        // Appel du service
        OfferDTO result = offerService.createOffer(offerDTO);

        // Vérifications
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Offer", result.getName());

        verify(offerRepository, times(1)).save(offer);
    }

    @Test
    void testGetOfferById_Success() throws Exception {
        // Mock des méthodes du repository et mapper
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
        when(offerMapper.toDTO(offer)).thenReturn(offerDTO);

        // Appel du service
        OfferDTO result = offerService.getOfferById(1L);

        // Vérifications
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Offer", result.getName());

        verify(offerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOfferById_OfferNotFound() {
        // Mock pour retourner un Optional vide
        when(offerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            offerService.getOfferById(1L);
        });

        assertEquals("Offer non trouvée", exception.getMessage());
        verify(offerRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateOffer_Success() throws Exception {
        // Mock des méthodes du repository et mapper
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
        when(offerRepository.save(offer)).thenReturn(offer);
        when(offerMapper.toDTO(offer)).thenReturn(offerDTO);

        // Appel du service
        OfferDTO result = offerService.updateOffer(1L, offerDTO);

        // Vérifications
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Offer", result.getName());

        verify(offerRepository, times(1)).findById(1L);
        verify(offerRepository, times(1)).save(offer);
    }

    @Test
    void testUpdateOffer_OfferNotFound() {
        // Mock pour retourner un Optional vide
        when(offerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            offerService.updateOffer(1L, offerDTO);
        });

        assertEquals("Offer non trouvée", exception.getMessage());
        verify(offerRepository, times(1)).findById(1L);
        verify(offerRepository, times(0)).save(any(Offer.class));
    }

    @Test
    void testDeleteOffer_Success() throws Exception {
        // Mock pour vérifier l'existence de l'offre
        when(offerRepository.existsById(1L)).thenReturn(true);

        // Appel du service
        offerService.deleteOffer(1L);

        // Vérifications
        verify(offerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteOffer_OfferNotFound() {
        // Mock pour vérifier que l'offre n'existe pas
        when(offerRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            offerService.deleteOffer(1L);
        });

        assertEquals("Offer non trouvée", exception.getMessage());
        verify(offerRepository, times(1)).existsById(1L);
        verify(offerRepository, times(0)).deleteById(1L);
    }
}
