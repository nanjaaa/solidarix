package com.solidarix.backend.controller;

import com.solidarix.backend.dto.HelpFeedbackCreationDto;
import com.solidarix.backend.dto.HelpFeedbackDto;
import com.solidarix.backend.security.CustomUserDetails;
import com.solidarix.backend.service.HelpFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks/")
public class HelpFeedbackController {

    private final HelpFeedbackService helpFeedbackService;

    // Lister tous les feedbacks d'une HelpOffer
    @GetMapping("/offer/{helpOfferId}")
    public ResponseEntity<List<HelpFeedbackDto>> getFeedbacksForOffer(
            @PathVariable Long helpOfferId
    ) {
        List<HelpFeedbackDto> feedbacks = helpFeedbackService.getFeedbacksForOffer(helpOfferId);
        return ResponseEntity.ok(feedbacks);
    }
}
