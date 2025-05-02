package com.solidarix.backend.controller;

import com.solidarix.backend.dto.HelpRequestDto;
import com.solidarix.backend.model.HelpRequest;
import com.solidarix.backend.model.User;
import com.solidarix.backend.security.CustomUserDetails;
import com.solidarix.backend.service.HelpRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/help-request")
public class HelpRequestController {

    private final HelpRequestService helpRequestService;

    public HelpRequestController(HelpRequestService helpRequestService) {
        this.helpRequestService = helpRequestService;
    }

    @PostMapping("/create")
    public ResponseEntity<HelpRequest> createHelpRequest(
            @RequestBody HelpRequestDto helpRequestDto
            , @AuthenticationPrincipal CustomUserDetails userDetails){

        User user = userDetails.getUser();
        HelpRequest helpRequest = helpRequestService.createHelpRequest(user, helpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(helpRequest);
    }

}
