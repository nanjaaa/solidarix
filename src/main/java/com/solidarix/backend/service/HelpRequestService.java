package com.solidarix.backend.service;

import com.solidarix.backend.dto.HelpRequestCommentDto;
import com.solidarix.backend.dto.HelpRequestCreationDto;
import com.solidarix.backend.dto.PrivateHelpRequestDto;
import com.solidarix.backend.dto.PublicHelpRequestDto;
import com.solidarix.backend.model.*;
import com.solidarix.backend.repository.HelpRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public HelpRequest createHelpRequest(User creator, HelpRequestCreationDto helpRequestCreationDto){

        Location location = locationService.findOrCreateLocation(helpRequestCreationDto.getAddress());

        if (creator == null || location == null) {
            throw new IllegalArgumentException("L'utilisateur ou la localisation ne peut être nul");
        }

        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setRequester(creator);
        helpRequest.setCategory(HelpCategory.valueOf(helpRequestCreationDto.getCategory()));
        helpRequest.setLocation(location);
        helpRequest.setHelpDate(helpRequestCreationDto.getHelpDate());
        helpRequest.setCreatedAt(LocalDateTime.now());
        helpRequest.setStatus(HelpStatus.WAITING_FOR_PROPOSAL);
        helpRequest.setDescription(helpRequestCreationDto.getDescription());

        return helpRequestRepository.save(helpRequest);
    }

    public HelpRequest findById(Long helpRequestId){

        return helpRequestRepository.findById(helpRequestId)
                .orElseThrow(() -> new RuntimeException("Help request not found"));

    }

    public HelpRequest save(HelpRequest helpRequest){
        return helpRequestRepository.save(helpRequest);
    }

    public List<HelpRequest> getNearbyFeed(double latitude, double longitude, double radiusKm) {
        return helpRequestRepository.findNearbyFeed(latitude, longitude, radiusKm);
    }

    private List<HelpRequestCommentDto> buildStructuredComments(HelpRequest request) {
        List<HelpRequestComment> all = request.getComments();

        // Création de la structure à 2 niveaux
        return all.stream()
                .filter(comment -> comment.getParentComment() == null) // Commentaires principaux
                .map(parent -> {
                    // On cherche les réponses à ce commentaire
                    List<HelpRequestCommentDto> replies = all.stream()
                            .filter(reply -> reply.getParentComment() != null &&
                                    reply.getParentComment().getId().equals(parent.getId()))
                            .map(reply -> HelpRequestCommentDto.fromEntity(reply, List.of()))
                            .toList();

                    return HelpRequestCommentDto.fromEntity(parent, replies);
                })
                .toList();
    }

    public boolean canAcceptHelpOffer(HelpRequest helpRequest){
        return List.of(HelpStatus.WAITING_FOR_PROPOSAL, HelpStatus.IN_DISCUSSION).contains(helpRequest.getStatus());
    }



    /*
    public List<?> getFeedForUser(User currentUser) {
        List<HelpRequest> requests = helpRequestRepository.;

        return requests.stream()
                .map(request -> {
                    boolean isOwner = request.getRequester().getId().equals(currentUser.getId());

                    boolean hasProposed = helpOfferService.hasUserProposedToHelp(currentUser, request);

                    List<HelpRequestCommentDto> comments = buildStructuredComments(request);

                    if (isOwner || hasProposed) {
                        return new PrivateHelpRequestDto(
                                request.getId(),
                                request.getRequester().getFirstName(),
                                request.getRequester().getLastName(),
                                request.getCategory().name(),
                                request.getDescription(),
                                request.getHelpDate(),
                                request.getCreatedAt(),
                                request.getStatus().name(),
                                comments,
                                request.getLocation().getFullAddress()
                        );
                    } else {
                        return new PublicHelpRequestDto(
                                request.getId(),
                                request.getRequester().getFirstName(),
                                request.getRequester().getLastName(),
                                request.getCategory().name(),
                                request.getDescription(),
                                request.getHelpDate(),
                                request.getCreatedAt(),
                                request.getStatus().name(),
                                comments
                        );
                    }
                })
                .toList();
    }
    */

    public List<HelpRequest> getFeedForUser(User currentUser) {
        String userPostalCode = currentUser.getAddress().getPostalCode();
        if (userPostalCode == null || userPostalCode.length() < 2) {
            return List.of(); // ou throw exception
        }

        String userRegionPrefix = userPostalCode.substring(0, 2);

        List<HelpRequest> requests = helpRequestRepository.findAll().stream()
                .filter(hr -> {
                    String pc = hr.getLocation() != null ? hr.getLocation().getPostalCode() : null;
                    return pc != null && pc.startsWith(userRegionPrefix);
                })
                .sorted(Comparator
                        .comparing((HelpRequest hr) -> hr.getStatus() != HelpStatus.WAITING_FOR_PROPOSAL) // WAITING d’abord
                        .thenComparingInt(hr -> -PostalCodeUtils.proximityScore(userPostalCode, hr.getLocation().getPostalCode()))
                        .thenComparing(HelpRequest::getCreatedAt, Comparator.reverseOrder())
                )
                .collect(Collectors.toList());

        return requests;
    }



}
