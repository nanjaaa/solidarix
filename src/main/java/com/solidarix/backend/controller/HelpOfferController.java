package com.solidarix.backend.controller;

import com.solidarix.backend.dto.HelpOfferCreationDto;
import com.solidarix.backend.dto.HelpOfferMessageDto;
import com.solidarix.backend.dto.HelpRequestCommentDto;
import com.solidarix.backend.model.HelpOffer;
import com.solidarix.backend.model.HelpOfferMessage;
import com.solidarix.backend.model.HelpRequest;
import com.solidarix.backend.model.User;
import com.solidarix.backend.security.CustomUserDetails;
import com.solidarix.backend.service.HelpOfferService;
import com.solidarix.backend.service.HelpRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/help-offer")
public class HelpOfferController {

    private final HelpOfferService helpOfferService;
    private final HelpRequestService helpRequestService;

    public HelpOfferController(HelpOfferService helpOfferService, HelpRequestService helpRequestService) {
        this.helpOfferService = helpOfferService;
        this.helpRequestService = helpRequestService;
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

}
