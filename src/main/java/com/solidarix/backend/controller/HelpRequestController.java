package com.solidarix.backend.controller;

import com.solidarix.backend.dto.HelpRequestCreationDto;
import com.solidarix.backend.dto.PublicHelpRequestDto;
import com.solidarix.backend.model.HelpRequest;
import com.solidarix.backend.model.User;
import com.solidarix.backend.security.CustomUserDetails;
import com.solidarix.backend.service.HelpRequestService;
import com.solidarix.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/help-request")
public class HelpRequestController {

    private final HelpRequestService helpRequestService;
    private final UserService userService;

    public HelpRequestController(HelpRequestService helpRequestService, UserService userService) {
        this.helpRequestService = helpRequestService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<HelpRequest> createHelpRequest(
            @RequestBody HelpRequestCreationDto helpRequestCreationDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        User user = userDetails.getUser();
        HelpRequest helpRequest = helpRequestService.createHelpRequest(user, helpRequestCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(helpRequest);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PublicHelpRequestDto>> getFeed(@AuthenticationPrincipal CustomUserDetails userDetails) {

        User currentUser = userDetails.getUser();
        List<HelpRequest> results = helpRequestService.getFeedForUser(currentUser);

        List<PublicHelpRequestDto> dtos = results.stream()
                .map(PublicHelpRequestDto::fromEntity)
                .toList();

        return ResponseEntity.ok(dtos);
    }



}
