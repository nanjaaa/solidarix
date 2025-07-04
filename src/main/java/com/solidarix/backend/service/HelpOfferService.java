package com.solidarix.backend.service;

import com.solidarix.backend.dto.HelpOfferCancellationDto;
import com.solidarix.backend.dto.HelpOfferCreationDto;
import com.solidarix.backend.dto.HelpOfferDto;
import com.solidarix.backend.dto.HelpOfferMessageCreationDto;
import com.solidarix.backend.model.*;
import com.solidarix.backend.repository.HelpOfferMessageRepository;
import com.solidarix.backend.repository.HelpOfferRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HelpOfferService {

    private final HelpOfferRepository helpOfferRepository;
    private final HelpOfferMessageService messageService;
    public final HelpRequestService helpRequestService;

    public HelpOfferService(HelpOfferRepository helpOfferRepository, HelpOfferMessageService messageService, HelpRequestService helpRequestService) {
        this.helpOfferRepository = helpOfferRepository;
        this.messageService = messageService;
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
        if (!helpRequestService.canAcceptHelpOffer(helpRequest)) {
            throw new IllegalStateException("Cette demande d'aide n'accepte plus de propositions.");
        }

        String firstMessageContent = helpOfferCreationDto.getFirstMessage();
        // setting help offer
        HelpOffer helpOffer = new HelpOffer();
        helpOffer.setHelper(helper);
        helpOffer.setHelpRequest(helpRequest);
        helpOffer.setCreatedAt(LocalDateTime.now());
        helpOffer.setExpirationReference(LocalDateTime.now());
        helpOffer.setStatus(HelpOfferStatus.PROPOSED);
        helpOfferRepository.save(helpOffer);

        // setting the first message
        HelpOfferMessageCreationDto messageDto = new HelpOfferMessageCreationDto();
        messageDto.setHelpOfferId(helpOffer.getId());
        messageDto.setMessage(helpOfferCreationDto.getFirstMessage());
        messageService.addMessageToHelpOffer(helper, messageDto);

        // Update Help Request Status
        this.actualizeHelpRequestStatus(helpOffer.getHelpRequest());
        helpRequestService.save(helpRequest);

        return helpOffer;
    }

    public boolean hasUserProposedToHelp(User user, HelpRequest request) {
        return helpOfferRepository.existsByHelperAndHelpRequest(user, request);
    }

    private List<HelpOffer> getHelpOffersForRequest(HelpRequest helpRequest) {
        return helpOfferRepository.findByHelpRequestId(helpRequest.getId());
    }

    private List<HelpOfferStatus> extractAllHelpOfferStatus(HelpRequest helpRequest) {

        List<HelpOffer> helpOffers = this.getHelpOffersForRequest(helpRequest);
        return helpOffers.stream()
                .map(HelpOffer::getStatus)
                .toList();
    }

    // Si on a des status annulé, expiré, échoué de HelpOffer, on tient compte des autres status et s'il n'y en a pas d'autres on mets WAITING_FOR_PROPOSAL
    private HelpStatus getActualHelpStatus(HelpRequest helpRequest){

        List<HelpOfferStatus> allHelpOfferStatus = this.extractAllHelpOfferStatus(helpRequest);

        if(allHelpOfferStatus.contains(HelpOfferStatus.DONE)){
            return HelpStatus.DONE;
        }
        else if(allHelpOfferStatus.contains(HelpOfferStatus.CONFIRMED_BY_HELPER)){
            return HelpStatus.CONFIRMED;
        }
        else if(allHelpOfferStatus.contains(HelpOfferStatus.PROPOSED)
                || allHelpOfferStatus.contains(HelpOfferStatus.VALIDATED_BY_REQUESTER)){
            return HelpStatus.IN_DISCUSSION;
        }

        return HelpStatus.WAITING_FOR_PROPOSAL;
    }

    private void actualizeHelpRequestStatus(HelpRequest helpRequest){
        HelpStatus actualHelpStatus = this.getActualHelpStatus(helpRequest);
        helpRequest.setStatus(actualHelpStatus);
        helpRequestService.save(helpRequest);
    }

    // Pour récupérer les discussions concernant un helpOffer
    public HelpOfferDto getDiscussion(HelpOffer helpOffer, User discussionViewer) {

        // S'il celui qui fait la requête pour voir la discussion sur un helpOffer est le requester
        //  -> il aura un dto PRIVATE du helpRequest, sinon il aura un dto public
        if (discussionViewer.equals(helpOffer.getHelpRequest().getRequester())){
            return HelpOfferDto.fromHelpOfferEntityToMyOwnHelpRequest(helpOffer);
        }
        return HelpOfferDto.fromHelpOfferEntityToOthersHelpRequest(helpOffer);
    }

    public HelpOfferDto getDiscussionById(Long helpOfferId, User currentUser) {

        HelpOffer helpOffer = helpOfferRepository.findById(helpOfferId)
                .orElseThrow(() -> new RuntimeException("Help Offer not found"));

        // Vérifie que l'utilisateur est concerné
        boolean isRequester = helpOffer.getHelpRequest().getRequester().getId().equals(currentUser.getId());
        boolean isHelper = helpOffer.getHelper().getId().equals(currentUser.getId());

        if (!isRequester && !isHelper) {
            throw new RuntimeException("Vous n'avez pas accès à cette discussion");
        }
        return HelpOfferDto.fromHelpOfferEntityToMyOwnHelpRequest(helpOffer);
    }


    // cette méthode renvoie la liste de toutes les discussions sur un helpOffer où un user prends part
    public List<HelpOfferDto> getDiscussionsForUser (User user){

        List<HelpOfferDto> discussionsDto = new ArrayList<>();

        List<HelpOffer> helpOffers = helpOfferRepository.findAllByUserInvolved(user.getId());
        for (HelpOffer ho : helpOffers){
            discussionsDto.add(this.getDiscussion(ho, user));
        }

        return discussionsDto;
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

        this.actualizeHelpRequestStatus(helpOffer.getHelpRequest());
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

        this.actualizeHelpRequestStatus(helpOffer.getHelpRequest());
        return helpOfferRepository.save(helpOffer);
    }


    @Transactional
    private HelpOffer cancelByRequester(User requester, Long helpOfferId, HelpOfferCancellationDto cancellationDto) {

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

        this.actualizeHelpRequestStatus(helpOffer.getHelpRequest());
        return helpOfferRepository.save(helpOffer);
    }


    @Transactional
    private HelpOffer cancelByHelper(User helper, Long helpOfferId, HelpOfferCancellationDto cancellationDto) {

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

        this.actualizeHelpRequestStatus(helpOffer.getHelpRequest());
        return helpOfferRepository.save(helpOffer);
    }


    @Transactional
    public HelpOffer cancelDependingOnUser(User user, Long helpOfferId, HelpOfferCancellationDto dto) {
        HelpOffer helpOffer = helpOfferRepository.findById(helpOfferId)
                .orElseThrow(() -> new RuntimeException("Help Offer not found"));

        if (user.equals(helpOffer.getHelpRequest().getRequester())) {
            return cancelByRequester(user, helpOfferId, dto);
        } else if (user.equals(helpOffer.getHelper())) {
            return cancelByHelper(user, helpOfferId, dto);
        } else {
            throw new RuntimeException("User is neither the requester nor the helper of this offer.");
        }
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

        this.actualizeHelpRequestStatus(helpOffer.getHelpRequest());
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

        this.actualizeHelpRequestStatus(helpOffer.getHelpRequest());
        return helpOfferRepository.save(helpOffer);
    }

}
