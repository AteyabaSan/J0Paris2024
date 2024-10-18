package com.joparis2024.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joparis2024.dto.PaymentDTO;
import com.joparis2024.mapper.PaymentMapper;
import com.joparis2024.model.Order;
import com.joparis2024.model.Payment;
import com.joparis2024.model.Ticket;
import com.stripe.model.PaymentIntent;

import jakarta.transaction.Transactional;

@Service
public class PaymentManagementFacade {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private PaymentMapper paymentMapper;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private TicketService ticketService;

    private static final Logger logger = LoggerFactory.getLogger(PaymentManagementFacade.class);
    

    @Transactional
    public PaymentDTO createPaymentWithStripe(Long orderId, Double amount, String currency) throws Exception {
        // Récupérer la commande associée
        Order orderEntity = orderService.findOrderEntityById(orderId);
        if (orderEntity == null) {
            throw new Exception("Commande non trouvée.");
        }

        // Créer un PaymentIntent via Stripe
        PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount, currency);

        // Créer un Payment associé à la commande
        Payment payment = new Payment();
        payment.setOrder(orderEntity);
        payment.setAmount(amount);
        payment.setPaymentMethod("STRIPE");
        payment.setPaymentStatus("EN_ATTENTE");
        payment.setPaymentDate(LocalDateTime.now());

        // **Assigner l'ID du PaymentIntent**
        payment.setPaymentIntentId(paymentIntent.getId());

        // Sauvegarder le paiement dans la base de données
        Payment savedPayment = paymentService.createPayment(payment);

        // Retourner le DTO du paiement sauvegardé
        return paymentMapper.toDTO(savedPayment);
    }

    
    @Transactional
    public PaymentDTO confirmPaymentWithStripe(Long paymentId, String cardNumber, String expMonth, String expYear, String cvc) throws Exception {
        // Récupère le paiement dans la base de données à partir de l'ID local
        Payment payment = paymentService.getPaymentEntityById(paymentId);  
        if (payment == null) {
            throw new Exception("Paiement non trouvé.");
        }

        // Récupère le PaymentIntentId à partir de l'objet Payment
        String paymentIntentId = payment.getPaymentIntentId();

        // Appel du service Stripe pour confirmer le PaymentIntent avec les détails de la carte
        PaymentIntent confirmedPaymentIntent = stripeService.confirmPayment(paymentIntentId, cardNumber, expMonth, expYear, cvc);

        // Mise à jour du statut du paiement en fonction de la réponse de Stripe
        if ("succeeded".equals(confirmedPaymentIntent.getStatus())) {
            payment.setPaymentStatus("RÉUSSI");

            // Mise à jour du statut de la commande liée au paiement
            Order order = payment.getOrder();
            if (order != null) {
                order.setStatus("CONFIRMÉE");
                orderService.updateOrder(order.getId(), order);  // Met à jour la commande
            }
        } else {
            payment.setPaymentStatus("ÉCHOUÉ");
        }

        // Mise à jour du paiement dans la base de données
        paymentService.updatePayment(payment.getId(), payment);

        // Retourne l'objet PaymentDTO correspondant
        return paymentMapper.toDTO(payment);
    }
    
 // Nouvelle méthode pour traiter le paiement et envoyer les tickets après validation
    @Transactional
    public void processPaymentAndSendTickets(Long orderId) throws Exception {
        logger.info("Traitement du paiement et envoi des tickets pour la commande ID: {}", orderId);

        // Récupérer la commande par son ID
        Order order = orderService.findOrderEntityById(orderId);
        if (order == null) {
            throw new Exception("Commande non trouvée avec l'ID : " + orderId);
        }

        // Récupérer les tickets associés à la commande
        List<Ticket> tickets = order.getTickets();

        // Parcourir chaque ticket et générer un QR code
        for (Ticket ticket : tickets) {
            try {
                // Générer un QR code pour chaque ticket
                String qrCodeText = "Ticket ID: " + ticket.getId() + ", Event: " + ticket.getEvent().getName();
                byte[] qrCode = ticketService.generateQRCode(qrCodeText, 300, 300);

                // Envoyer l'e-mail avec le QR code en pièce jointe
                emailService.sendEmailWithTicket(order.getUser().getEmail(),
                    "Votre Ticket pour l'événement : " + ticket.getEvent().getName(),
                    "Veuillez trouver ci-joint votre ticket sous forme de QR code.",
                    qrCode);

                logger.info("QR code envoyé avec succès pour le ticket ID : {}", ticket.getId());
            } catch (Exception e) {
                logger.error("Erreur lors de l'envoi des tickets pour la commande ID : " + order.getId(), e);
                throw new Exception("Erreur lors de l'envoi des tickets.", e);
            }
        }
    }


    public PaymentDTO getPaymentDetails(Long paymentId) throws Exception {
        // Récupérer les détails d'un paiement avec ses informations de commande
        return paymentService.getPaymentById(paymentId);
    }

    public List<PaymentDTO> getAllPayments() throws Exception {
        // Récupérer tous les paiements
        return paymentService.getAllPayments();
    }

    public PaymentDTO updatePayment(Long paymentId, PaymentDTO paymentDTO) throws Exception {
        // Mise à jour d'un paiement existant
        return paymentService.updatePayment(paymentId, paymentDTO);
    }

    public void cancelPayment(Long paymentId) throws Exception {
        // Annulation d'un paiement
        paymentService.cancelPayment(paymentId);
    }
}
