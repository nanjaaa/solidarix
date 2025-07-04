package com.solidarix.backend.controller;

import com.solidarix.backend.dto.HelpOfferMessageCreationDto;
import com.solidarix.backend.model.HelpOfferMessage;
import com.solidarix.backend.model.User;
import com.solidarix.backend.security.CustomUserDetails;
import com.solidarix.backend.service.HelpOfferMessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/help-offer-message")
public class HelpOfferMessageController {

    private final HelpOfferMessageService helpOfferMessageService;

    public HelpOfferMessageController(HelpOfferMessageService helpOfferMessageService) {
        this.helpOfferMessageService = helpOfferMessageService;
    }

    /**
     * Envoyer un message dans une proposition d'aide
     */
    @PostMapping("/send")
    public ResponseEntity<HelpOfferMessage> addMessageToHelpOffer(
            @Valid @RequestBody HelpOfferMessageCreationDto helpOfferMessageCreationDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User sender = userDetails.getUser();
        HelpOfferMessage message = helpOfferMessageService.addMessageToHelpOffer(sender, helpOfferMessageCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    /**
     * Récupérer tous les messages pour une proposition d'aide
     */
    @GetMapping("/all/{helpOfferId}")
    public ResponseEntity<List<HelpOfferMessage>> getAllMessagesForHelpOffer(
            @PathVariable Long helpOfferId
    ) {
        List<HelpOfferMessage> messages = helpOfferMessageService.findAllMessagesForHelpOffer(helpOfferId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Marquer tous les messages reçus par l'utilisateur dans un helpOffer comme lus
     */
    @PostMapping("/{helpOfferId}/mark-all-as-read")
    public ResponseEntity<Void> markAllMessagesAsRead(
            @PathVariable Long helpOfferId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        helpOfferMessageService.markAllMessagesAsReadForUser(user, helpOfferId);
        return ResponseEntity.ok().build();
    }
}