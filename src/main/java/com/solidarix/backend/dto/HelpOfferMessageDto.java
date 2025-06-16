package com.solidarix.backend.dto;

import com.solidarix.backend.model.HelpOfferMessage;
import com.solidarix.backend.model.HelpRequestComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class HelpOfferMessageDto {

    private Long id;
    private UserSimpleDto sender;
    private String content;
    private LocalDateTime createdAt;

    public static HelpOfferMessageDto fromEntity(HelpOfferMessage message) {
        return new HelpOfferMessageDto(
                message.getId(),
                UserSimpleDto.fromEntity(message.getSender()),
                message.getMessage(),
                message.getSentAt()
        );
    }

}

