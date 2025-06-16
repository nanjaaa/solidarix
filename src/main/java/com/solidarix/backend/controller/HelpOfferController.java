package com.solidarix.backend.controller;

import com.solidarix.backend.dto.HelpOfferCancellationDto;
import com.solidarix.backend.dto.HelpOfferCreationDto;
import com.solidarix.backend.dto.HelpOfferDto;
import com.solidarix.backend.dto.HelpOfferMessageCreationDto;
import com.solidarix.backend.model.HelpOffer;
import com.solidarix.backend.model.HelpOfferMessage;
import com.solidarix.backend.model.User;
import com.solidarix.backend.security.CustomUserDetails;
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


    public HelpOfferController(HelpOfferService helpOfferService) {
        this.helpOfferService = helpOfferService;
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

        HelpOffer helpOffer = helpOfferService.createHelpOffer(helper, helpOfferCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(helpOffer);
    }


    @PostMapping("/send-message")
    public ResponseEntity<HelpOfferMessage> addMessageToHelpOffer(
            @RequestBody HelpOfferMessageCreationDto helpOfferMessageCreationDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
            ){

        User sender = userDetails.getUser();

        HelpOfferMessage message = helpOfferService.addMessageToHelpOffer(sender, helpOfferMessageCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }


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
    public ResponseEntity<HelpOffer> markAsDone(
            @PathVariable Long helpOfferId
            , @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        User requester = userDetails.getUser();
        HelpOffer closedOffer = helpOfferService.markAsDone(requester, helpOfferId);
        return ResponseEntity.ok(closedOffer);
    }

}
