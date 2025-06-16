package com.solidarix.backend.dto;

import com.solidarix.backend.model.HelpOffer;
import com.solidarix.backend.model.HelpOfferMessage;
import com.solidarix.backend.model.HelpRequest;
import com.solidarix.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
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

    public HelpOfferDto(
            Long helpOffer_id, HelpRequest help_request, User offerer,
            LocalDateTime created_at, LocalDateTime expiration_reference, String status,
            LocalDateTime closed_at, LocalDateTime canceled_at, String cancellation_justification,
            List<HelpOfferMessage> messages) {

        this.helpOfferId                  = helpOffer_id;
        this.helpRequest                  = PrivateHelpRequestDto.fromEntity(help_request);
        this.offerer                       = UserSimpleDto.fromEntity(offerer);
        this.createdAt                    = created_at;
        this.expirationReference          = expiration_reference;
        this.status                        = status;
        this.closedAt                     = closed_at;
        this.canceledAt                   = canceled_at;
        this.cancellationJustification    = cancellation_justification;
        this.messages = messages.stream()
                .map(HelpOfferMessageDto::fromEntity)
                .toList();
    }

    public static HelpOfferDto fromHelpOfferEntityToMyOwnHelpRequest(HelpOffer helpOffer){
        return new HelpOfferDto(
                helpOffer.getId(),
                helpOffer.getHelpRequest(),
                helpOffer.getHelper(),
                helpOffer.getCreatedAt(),
                helpOffer.getExpirationReference(),
                helpOffer.getStatus().name(),
                helpOffer.getClosedAt(),
                helpOffer.getCanceledAt(),
                helpOffer.getCancellationJustification(),
                helpOffer.getMessages()
        );
    }

    public static HelpOfferDto fromHelpOfferEntityToOthersHelpRequest(HelpOffer helpOffer){
        HelpOfferDto dto = new HelpOfferDto(
                helpOffer.getId(),
                helpOffer.getHelpRequest(),
                helpOffer.getHelper(),
                helpOffer.getCreatedAt(),
                helpOffer.getExpirationReference(),
                helpOffer.getStatus().name(),
                helpOffer.getClosedAt(),
                helpOffer.getCanceledAt(),
                helpOffer.getCancellationJustification(),
                helpOffer.getMessages()
        );
        dto.setHelpRequest((PublicHelpRequestDto)dto.getHelpRequest());
        return dto;
    }

}
