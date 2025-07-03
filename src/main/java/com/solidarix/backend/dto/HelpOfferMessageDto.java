package com.solidarix.backend.dto;

import com.solidarix.backend.model.HelpOfferMessage;
import com.solidarix.backend.model.HelpRequestComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class HelpOfferMessageDto extends MessageDto {

    public HelpOfferMessageDto(Long id, UserSimpleDto sender, String content, LocalDateTime createdAt) {
        super(id, sender, content, createdAt, null, true, false, false); // only isAboutHelpOffer=true
    }

    public static HelpOfferMessageDto fromEntity(HelpOfferMessage message) {
        HelpOfferMessageDto dto = new HelpOfferMessageDto(
                message.getId(),
                UserSimpleDto.fromEntity(message.getSender()),
                message.getMessage(),
                message.getSentAt()
        );
        dto.setSeenAt(message.getSeenAt());
        return dto;
    }

}

