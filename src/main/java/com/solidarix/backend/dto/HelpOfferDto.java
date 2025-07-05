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

    public static HelpOfferDto fromHelpOfferEntityWithPrivateHelpRequest(HelpOffer helpOffer){
        List<HelpOfferMessageDto> messageDtos = new ArrayList<>();
        for(HelpOfferMessage m : helpOffer.getMessages()){
            messageDtos.add(HelpOfferMessageDto.fromEntity(m));
        }
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
                messageDtos
        );
    }

    public static HelpOfferDto fromHelpOfferEntityWithPublicHelpRequest(HelpOffer helpOffer){
        List<HelpOfferMessageDto> messageDtos = new ArrayList<>();
        for(HelpOfferMessage m : helpOffer.getMessages()){
            messageDtos.add(HelpOfferMessageDto.fromEntity(m));
        }
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
                messageDtos
        );
    }

}
