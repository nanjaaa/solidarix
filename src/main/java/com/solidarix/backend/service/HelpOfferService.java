package com.solidarix.backend.service;

import com.solidarix.backend.dto.HelpOfferCancellationDto;
import com.solidarix.backend.dto.HelpOfferCreationDto;
import com.solidarix.backend.dto.HelpOfferMessageDto;
import com.solidarix.backend.model.*;
import com.solidarix.backend.repository.HelpOfferMessageRepository;
import com.solidarix.backend.repository.HelpOfferRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HelpOfferService {

    private final HelpOfferRepository helpOfferRepository;
    private final HelpOfferMessageRepository messageRepository;
    public final HelpRequestService helpRequestService;

    public HelpOfferService(HelpOfferRepository helpOfferRepository, HelpOfferMessageRepository messageRepository, HelpRequestService helpRequestService) {
        this.helpOfferRepository = helpOfferRepository;
        this.messageRepository = messageRepository;
        this.helpRequestService = helpRequestService;
    }

    public HelpOffer findById(Long id) {
        return helpOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Help Offer not found"));
    }

    @Transactional
    public HelpOffer createHelpOffer(User helper, HelpOfferCreationDto helpOfferCreationDto) {
        // Quand un HelpOffer se crée, il y a un premier message qui l'accompagne

        HelpRequest helpRequest = helpRequestService.findById(helpOfferCreationDto.getHelpRequestId());
        String firstMessageContent = helpOfferCreationDto.getFirstMessage();
        // setting help offer
        HelpOffer helpOffer = new HelpOffer();
        helpOffer.setHelper(helper);
        helpOffer.setHelpRequest(helpRequest);
        helpOffer.setCreatedAt(LocalDateTime.now());
        helpOffer.setExpirationReference(LocalDateTime.now());
        helpOffer.setStatus(HelpOfferStatus.PROPOSED);

        // setting th first message
        HelpOfferMessage message = new HelpOfferMessage();
        message.setHelpOffer(helpOffer);
        message.setSender(helper);
        message.setMessage(firstMessageContent);
        message.setSentAt(LocalDateTime.now());

        // Update Help Request Status
        helpRequest.setStatus(HelpStatus.IN_DISCUSSION);

        helpOfferRepository.save(helpOffer);
        messageRepository.save(message);
        helpRequestService.save(helpRequest);
        return helpOffer;
    }

    public boolean hasUserProposedToHelp(User user, HelpRequest request) {
        return helpOfferRepository.existsByHelperAndHelpRequest(user, request);
    }


    public HelpOfferMessage addMessageToHelpOffer(User sender, HelpOfferMessageDto helpOfferMessageDto) {

        String messageContent = helpOfferMessageDto.getMessage();
        HelpOffer helpOffer = helpOfferRepository.findById(helpOfferMessageDto.getHelpOfferId())
                .orElseThrow(() -> new RuntimeException("Help Offer not found"));

        HelpOfferMessage message = new HelpOfferMessage();
        message.setHelpOffer(helpOffer);
        message.setSender(sender);
        message.setMessage(messageContent);
        message.setSentAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    // Le demandeur d'aide valide une proposition d'aide
    public HelpOffer validateByRequester(User requester, Long helpOfferId) {

        HelpOffer helpOffer = helpOfferRepository.findById(helpOfferId)
                .orElseThrow(() -> new RuntimeException("Help Offer not found"));

        if (helpOffer.isExpired()) {
            helpOffer.setStatus(HelpOfferStatus.EXPIRED);
            return helpOfferRepository.save(helpOffer);
        }

        // Vérifier si le demandeur est bien celui qui a fait la demande
        if (!requester.equals(helpOffer.getHelpRequest().getRequester())) {
            throw new RuntimeException("Only requester can validate the help offer.");
        }

        // Vérifier que le statut actuel permet une validation
        if (helpOffer.getStatus() != HelpOfferStatus.PROPOSED) {
            throw new RuntimeException("Help offer is not in a state that allows validation.");
        }

        helpOffer.setStatus(HelpOfferStatus.VALIDATED_BY_REQUESTER);
        helpOffer.setExpirationReference(LocalDateTime.now());
        return helpOfferRepository.save(helpOffer);
    }


    @Transactional
    public HelpOffer confirmByHelper(User helper, Long helpOfferId) {

        HelpOffer helpOffer = helpOfferRepository.findById(helpOfferId)
                .orElseThrow(() -> new RuntimeException("Help Offer not found"));

        if (helpOffer.isExpired()) {
            helpOffer.setStatus(HelpOfferStatus.EXPIRED);
            return helpOfferRepository.save(helpOffer);
        }

        // Vérifie si celui qui confirme est bien celui qui a fati la proposition
        if (!helper.equals(helpOffer.getHelper())) {
            throw new RuntimeException("Only help offer creator can confirm the help offer.");
        }

        // Vérifier que le statut actuel permet une validation
        if (helpOffer.getStatus() != HelpOfferStatus.VALIDATED_BY_REQUESTER) {
            throw new RuntimeException("Help offer is not in a state that allows confirmation.");
        }

        helpOffer.getHelpRequest().setStatus(HelpStatus.CONFIRMED);
        helpRequestService.save(helpOffer.getHelpRequest());

        helpOffer.setStatus(HelpOfferStatus.CONFIRMED_BY_HELPER);
        helpOffer.setExpirationReference(null);
        return helpOfferRepository.save(helpOffer);
    }


    @Transactional
    public HelpOffer cancelByRequester(User requester, Long helpOfferId, HelpOfferCancellationDto cancellationDto) {

        String reason = cancellationDto.getJustification();
        HelpOffer helpOffer = helpOfferRepository.findById(helpOfferId)
                .orElseThrow(() -> new RuntimeException("Help Offer not found"));

        // Vérifier si le demandeur est bien celui qui a fait l'annulation
        if (!requester.equals(helpOffer.getHelpRequest().getRequester())) {
            throw new RuntimeException("Only requester can cancel (on mode cancel by requester).");
        }

        // Vérifier que le statut actuel permet une annulation par le demandeur
        switch (helpOffer.getStatus()) {
            case PROPOSED -> {
                // pas d'impact sur le score social, aucune validation ni confirmation n'a été faite
                // à implémenter plus tard
                System.out.println("(Status PROPOSED) IMPACT ANNULATION = 0");
            }
            case VALIDATED_BY_REQUESTER -> {
                // impacte x sur le score social, annulation après déjà validé
                // à implémenter plus tard
                System.out.println("(Status VALIDATED) IMPACT ANNULATION = 50");
            }
            case CONFIRMED_BY_HELPER -> {
                // impacte x+ sur le score social, annulation après avoir eu confirmation de la part du proposeur d'aide
                // à implémenter plus tard
                System.out.println("(Status CONFIRMED) IMPACT ANNULATION = 99");
            }
            default -> throw new RuntimeException("Help offer is not in a state that allows cancellation.");
        }

        helpOffer.getHelpRequest().setStatus(HelpStatus.WAITING_FOR_PROPOSAL);
        helpRequestService.save(helpOffer.getHelpRequest());

        helpOffer.setStatus(HelpOfferStatus.CANCELED_BY_REQUESTER);
        helpOffer.setCancellationJustification(reason);
        helpOffer.setCanceledAt(LocalDateTime.now());
        return helpOfferRepository.save(helpOffer);
    }


    @Transactional
    public HelpOffer cancelByHelper(User helper, Long helpOfferId, HelpOfferCancellationDto cancellationDto) {

        String reason = cancellationDto.getJustification();
        HelpOffer helpOffer = helpOfferRepository.findById(helpOfferId)
                .orElseThrow(() -> new RuntimeException("Help Offer not found"));

        // Vérifier si le demandeur est bien celui qui a fait l'annulation
        if (!helper.equals(helpOffer.getHelper())) {
            throw new RuntimeException("Only helper can cancel (on mode cancel by helper).");
        }

        // Vérifier que le statut actuel permet une annulation par le proposeur
        switch (helpOffer.getStatus()) {
            case PROPOSED -> {
                // pas d'impact sur le score social, aucune validation ni confirmation n'a été faite
                // à implémenter plus tard
                System.out.println("(Status PROPOSED) IMPACT ANNULATION = 0");
            }
            case VALIDATED_BY_REQUESTER -> {
                // pas d'impact sur le score social, retrait avant confirmation
                // à implémenter plus tard
                System.out.println("(Status VALIDATED) IMPACT ANNULATION = 0");
            }
            case CONFIRMED_BY_HELPER -> {
                // impacte x+ sur le score social, retrait après avoir confirmé
                // à implémenter plus tard
                System.out.println("(Status CNOFIRMED) IMPACT ANNULATION = 99");
            }
            default -> throw new RuntimeException("Help offer is not in a state that allows cancellation.");
        }

        helpOffer.getHelpRequest().setStatus(HelpStatus.WAITING_FOR_PROPOSAL);
        helpRequestService.save(helpOffer.getHelpRequest());

        helpOffer.setStatus(HelpOfferStatus.CANCELED_BY_HELPER);
        helpOffer.setCancellationJustification(reason);
        helpOffer.setCanceledAt(LocalDateTime.now());
        return helpOfferRepository.save(helpOffer);
    }


    @Transactional
    public HelpOffer markAsDone(User requester, Long helpOfferId) {

        HelpOffer helpOffer = helpOfferRepository.findById(helpOfferId)
                .orElseThrow(() -> new RuntimeException("Help Offer not found"));

        // Vérifier si celui qui veut clôturer l'entraide est bien le demandeur
        if (!requester.equals(helpOffer.getHelpRequest().getRequester())) {
            throw new RuntimeException("Only requester can mark help request as DONE.");
        }

        if (!helpOffer.getStatus().equals(HelpOfferStatus.CONFIRMED_BY_HELPER)) {
            throw new RuntimeException("Only validated by requester and confirmed by helper requests can be marked as DONE.");
        }

        // Ajouter l'avis après entraide et le système de score

        helpOffer.getHelpRequest().setStatus(HelpStatus.DONE);
        helpRequestService.save(helpOffer.getHelpRequest());

        helpOffer.setStatus(HelpOfferStatus.DONE);
        helpOffer.setClosedAt(LocalDateTime.now());
        return helpOfferRepository.save(helpOffer);
    }


    @Transactional
    public HelpOffer markAsFailed(User user, Long helpOfferId){

        HelpOffer helpOffer = helpOfferRepository.findById(helpOfferId)
                .orElseThrow(() -> new RuntimeException("Help Offer not found"));

        // Seul le demandeur et le proposeur d'aide peuvent définir qu'une entraide s'est échouée
        if (
                !user.equals(helpOffer.getHelpRequest().getRequester())
                && !user.equals(helpOffer.getHelper())
        ) {
            throw new RuntimeException("Only requester or helper can mark help request as Failed.");
        }

        if (!helpOffer.getStatus().equals(HelpOfferStatus.CONFIRMED_BY_HELPER)) {
            throw new RuntimeException("Only validated by requester and confirmed by helper requests can be marked as FAILED.");
        }

        helpOffer.getHelpRequest().setStatus(HelpStatus.WAITING_FOR_PROPOSAL);
        helpRequestService.save(helpOffer.getHelpRequest());

        helpOffer.setStatus(HelpOfferStatus.FAILED);
        helpOffer.setClosedAt(LocalDateTime.now());
        return helpOfferRepository.save(helpOffer);
    }

}
