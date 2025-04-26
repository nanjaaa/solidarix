package com.solidarix.backend.service;

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

    public HelpRequest createHelpRequest(User user, String category, String fullAddress, LocalDateTime helpDate, String description){

        Location location = locationService.findOrCreateLocation(fullAddress);

        if (user == null || location == null) {
            throw new IllegalArgumentException("L'utilisateur ou la localisation ne peut être nul");
        }

        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setRequester(user);
        helpRequest.setCategory(HelpCategory.valueOf(category));
        helpRequest.setLocation(location);
        helpRequest.setHelpDate(helpDate);
        helpRequest.setCreatedAt(LocalDateTime.now());
        helpRequest.setStatus(HelpStatus.EN_ATTENTE);
        helpRequest.setDescription(description);
        helpRequestRepository.save(helpRequest);

        return helpRequest;
    }

}
