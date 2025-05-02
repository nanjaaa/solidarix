package com.solidarix.backend.service;

import com.solidarix.backend.dto.HelpRequestDto;
import com.solidarix.backend.model.*;
import com.solidarix.backend.repository.HelpRequestRepository;
import com.solidarix.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HelpRequestService {

    private final HelpRequestRepository helpRequestRepository;
    private final UserService userService;
    private final LocationService locationService;

    public HelpRequestService(HelpRequestRepository helpRequestRepository, UserService userService, LocationService locationService) {
        this.helpRequestRepository = helpRequestRepository;
        this.userService = userService;
        this.locationService = locationService;
    }

    public HelpRequest createHelpRequest(User creator, HelpRequestDto helpRequestDto){

        Location location = locationService.findOrCreateLocation(helpRequestDto.getFullAddress());

        if (creator == null || location == null) {
            throw new IllegalArgumentException("L'utilisateur ou la localisation ne peut être nul");
        }

        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setRequester(creator);
        helpRequest.setCategory(HelpCategory.valueOf(helpRequestDto.getCategory()));
        helpRequest.setLocation(location);
        helpRequest.setHelpDate(helpRequestDto.getHelpDate());
        helpRequest.setCreatedAt(LocalDateTime.now());
        helpRequest.setStatus(HelpStatus.EN_ATTENTE);
        helpRequest.setDescription(helpRequestDto.getDescription());

        return helpRequestRepository.save(helpRequest);
    }

    public HelpRequest findById(Long helpRequestId){

        return helpRequestRepository.findById(helpRequestId)
                .orElseThrow(() -> new RuntimeException("Help request not found"));

    }

}
