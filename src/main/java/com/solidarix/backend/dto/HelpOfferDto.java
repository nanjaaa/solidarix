package com.solidarix.backend.dto;

import com.solidarix.backend.model.HelpIncidentReport;
import com.solidarix.backend.model.HelpOffer;
import com.solidarix.backend.model.HelpOfferMessage;
import com.solidarix.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class HelpOfferDto {

    private Long helpOfferId;
    private PublicHelpRequestDto helpRequest;
    private UserSimpleDto offerer;
    private LocalDateTime createdAt;
    private LocalDateTime expirationReference;
    private String status;
    private LocalDateTime closedAt;
    private LocalDateTime canceledAt;
    private String cancellationJustification;
    private List<HelpOfferMessageDto> messages;
    private boolean hasCurrentUserSubmittedFeedback;
    private boolean hasCurrentUserReportedIncident;
    private Boolean git;


    public static HelpOfferDto fromHelpOfferEntityWithPrivateHelpRequest(HelpOffer helpOffer, User currentUser){
        List<HelpOfferMessageDto> messageDtos = new ArrayList<>();
        for(HelpOfferMessage m : helpOffer.getMessages()){
            messageDtos.add(HelpOfferMessageDto.fromEntity(m));
        }

        // Trouver le premier incident
        HelpIncidentReport firstIncident = helpOffer.getIncidentReports()
                .stream()
                .min((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .orElse(null);

        Boolean isFirstReporter = (firstIncident != null)
                ? firstIncident.getReporter().getId().equals(currentUser.getId())
                : null;

        return new HelpOfferDto(
                helpOffer.getId(),
                PrivateHelpRequestDto.fromEntity(helpOffer.getHelpRequest()),
                UserSimpleDto.fromEntity(helpOffer.getHelper()),
                helpOffer.getCreatedAt(),
                helpOffer.getExpirationReference(),
                helpOffer.getStatus().name(),
                helpOffer.getClosedAt(),
                helpOffer.getCanceledAt(),
                helpOffer.getCancellationJustification(),
                messageDtos,
                helpOffer.getFeedbacks().stream().anyMatch(f -> f.getAuthor().getId().equals(currentUser.getId())),
                helpOffer.getIncidentReports().stream().anyMatch(f -> f.getReporter().getId().equals(currentUser.getId())),
                isFirstReporter
        );
    }

    public static HelpOfferDto fromHelpOfferEntityWithPublicHelpRequest(HelpOffer helpOffer, User currentUser){
        List<HelpOfferMessageDto> messageDtos = new ArrayList<>();

        for(HelpOfferMessage m : helpOffer.getMessages()){
            messageDtos.add(HelpOfferMessageDto.fromEntity(m));
        }

        // Trouver le premier incident
        HelpIncidentReport firstIncident = helpOffer.getIncidentReports()
                .stream()
                .min((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .orElse(null);

        Boolean isFirstReporter = (firstIncident != null)
                ? firstIncident.getReporter().getId().equals(currentUser.getId())
                : null;

        return new HelpOfferDto(
                helpOffer.getId(),
                PublicHelpRequestDto.fromEntity(helpOffer.getHelpRequest()),
                UserSimpleDto.fromEntity(helpOffer.getHelper()),
                helpOffer.getCreatedAt(),
                helpOffer.getExpirationReference(),
                helpOffer.getStatus().name(),
                helpOffer.getClosedAt(),
                helpOffer.getCanceledAt(),
                helpOffer.getCancellationJustification(),
                messageDtos,
                helpOffer.getFeedbacks().stream().anyMatch(f -> f.getAuthor().getId().equals(currentUser.getId())),
                helpOffer.getIncidentReports().stream().anyMatch(f -> f.getReporter().getId().equals(currentUser.getId())),
                isFirstReporter
        );
    }

}
