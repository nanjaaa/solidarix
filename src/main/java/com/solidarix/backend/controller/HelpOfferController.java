package com.solidarix.backend.controller;

import com.solidarix.backend.dto.*;
import com.solidarix.backend.model.HelpOffer;
import com.solidarix.backend.model.HelpOfferMessage;
import com.solidarix.backend.model.User;
import com.solidarix.backend.security.CustomUserDetails;
import com.solidarix.backend.service.HelpOfferMessageService;
import com.solidarix.backend.service.HelpOfferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/help-offer")
public class HelpOfferController {

    private final HelpOfferService helpOfferService;
    private final HelpOfferMessageService helpOfferMessageService;


    public HelpOfferController(HelpOfferService helpOfferService, HelpOfferMessageService helpOfferMessageService) {
        this.helpOfferService = helpOfferService;
        this.helpOfferMessageService = helpOfferMessageService;
    }

    @GetMapping("/{helpOfferId}")
    public ResponseEntity<HelpOfferDto> getHelpOfferDiscussion(
            @PathVariable Long helpOfferId,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        User currentUser = userDetails.getUser();
        HelpOfferDto discussionDto = helpOfferService.getDiscussionById(helpOfferId, currentUser);
        return ResponseEntity.ok(discussionDto);
    }

    @PostMapping("/create")
    public ResponseEntity<HelpOffer> createHelpOffer(
            @RequestBody HelpOfferCreationDto helpOfferCreationDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
            ){

        User helper = userDetails.getUser();

        try {
            HelpOffer helpOffer = helpOfferService.createHelpOffer(helper, helpOfferCreationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(helpOffer); //OK 200
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); //Error 409
        }
    }


    /*
    @PostMapping("/send-message")
    public ResponseEntity<HelpOfferMessage> addMessageToHelpOffer(
            @RequestBody HelpOfferMessageCreationDto helpOfferMessageCreationDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
            ){

        User sender = userDetails.getUser();

        HelpOfferMessage message = helpOfferMessageService.addMessageToHelpOffer(sender, helpOfferMessageCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
    */

    /*
    * Pour l'instant ce contrôleur ne renvoie que les discussions sur les HelpOffers,
    * plus tard il y aura aussi les discussions privées, sur des événements ....
     */
    @GetMapping("/my-discussions")
    public ResponseEntity<List<HelpOfferDto>> getMyDiscussions(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){

        User user = userDetails.getUser();
        List<HelpOfferDto> discussions = helpOfferService.getDiscussionsForUser(user);

        return ResponseEntity.ok(discussions);
    }


    @PostMapping("/{helpOfferId}/validate")
    public ResponseEntity<HelpOffer> validateByRequester(
            @PathVariable Long helpOfferId
            , @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        User validator = userDetails.getUser();
        HelpOffer validateHelpOffer = helpOfferService.validateByRequester(validator, helpOfferId);
        return ResponseEntity.ok(validateHelpOffer);
    }


    @PostMapping("/{helpOfferId}/confirm")
    public ResponseEntity<HelpOffer> confirmByHelper(
            @PathVariable Long helpOfferId
            , @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        User helper = userDetails.getUser();
        HelpOffer confirmedHelpOffer = helpOfferService.confirmByHelper(helper, helpOfferId);
        return ResponseEntity.ok(confirmedHelpOffer);
    }

    //public HelpOffer cancelDependingOnUser(User user, Long helpOfferId, HelpOfferCancellationDto dto) {
    @PostMapping("/{helpOfferId}/cancel")
    public ResponseEntity<HelpOffer> cancelHelpOffer(
            @PathVariable Long helpOfferId
            , @Valid @RequestBody HelpOfferCancellationDto cancellationDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        User canceler = userDetails.getUser();
        HelpOffer cancelledHelpOffer = helpOfferService.cancelDependingOnUser(canceler, helpOfferId, cancellationDto);

        return ResponseEntity.ok(cancelledHelpOffer);
    }

    @PostMapping("/{helpOfferId}/mark-as-done")
    public ResponseEntity<HelpOfferDto> markAsDone(
            @PathVariable Long helpOfferId
            , @RequestBody HelpFeedbackCreationDto feedBackDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        User requester = userDetails.getUser();
        HelpOfferDto closedOffer = helpOfferService.markAsDone(requester, helpOfferId, feedBackDto);
        return ResponseEntity.ok(closedOffer);
    }

    @PostMapping("/{helpOfferId}/add-feedback")
    public ResponseEntity<HelpOfferDto> addFeedback(
            @PathVariable Long helpOfferId
            , @RequestBody HelpFeedbackCreationDto feedBackDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        User helper = userDetails.getUser();
        HelpOfferDto closedOffer = helpOfferService.addHelperFeedback(helper, helpOfferId, feedBackDto);
        return ResponseEntity.ok(closedOffer);
    }

    @PostMapping("/{helpOfferId}/mark-as-failed")
    public ResponseEntity<HelpOfferDto> markAsFailed(
            @PathVariable Long helpOfferId
            , @RequestBody HelpIncidentReportCreationDto incidentReportDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        User reporter = userDetails.getUser();
        HelpOfferDto closedOffer = helpOfferService.markAsFailed(reporter, helpOfferId, incidentReportDto);
        return ResponseEntity.ok(closedOffer);
    }

    @PostMapping("/{helpOfferId}/report-incident")
    public ResponseEntity<HelpOfferDto> reportIncident(
            @PathVariable Long helpOfferId
            , @RequestBody HelpIncidentReportCreationDto incidentReportDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        User reporter = userDetails.getUser();
        HelpOfferDto closedOffer = helpOfferService.reportIncident(reporter, helpOfferId, incidentReportDto);
        return ResponseEntity.ok(closedOffer);
    }

}
