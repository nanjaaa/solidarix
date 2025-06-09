package com.solidarix.backend.controller;

import com.solidarix.backend.dto.HelpOfferCancellationDto;
import com.solidarix.backend.dto.HelpOfferCreationDto;
import com.solidarix.backend.dto.HelpOfferMessageDto;
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

@RestController
@RequestMapping("/help-offer")
public class HelpOfferController {

    private final HelpOfferService helpOfferService;


    public HelpOfferController(HelpOfferService helpOfferService) {
        this.helpOfferService = helpOfferService;
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
            @RequestBody HelpOfferMessageDto helpOfferMessageDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
            ){

        User sender = userDetails.getUser();

        HelpOfferMessage message = helpOfferService.addMessageToHelpOffer(sender, helpOfferMessageDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
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


    @PostMapping("/{helpOfferId}/cancel-by-requester")
    public ResponseEntity<HelpOffer> cancelByRequester(
            @PathVariable Long helpOfferId
            , @Valid @RequestBody HelpOfferCancellationDto cancellationDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        User canceler = userDetails.getUser();
        HelpOffer canceledHelpOffer = helpOfferService.cancelByRequester(canceler, helpOfferId, cancellationDto);
        return ResponseEntity.ok(canceledHelpOffer);
    }


    @PostMapping("/{helpOfferId}/cancel-by-helper")
    public ResponseEntity<HelpOffer> cancelByHelper(
            @PathVariable Long helpOfferId
            , @Valid @RequestBody HelpOfferCancellationDto cancellationDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        User canceler = userDetails.getUser();
        HelpOffer canceledHelpOffer = helpOfferService.cancelByHelper(canceler, helpOfferId, cancellationDto);
        return ResponseEntity.ok(canceledHelpOffer);
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
