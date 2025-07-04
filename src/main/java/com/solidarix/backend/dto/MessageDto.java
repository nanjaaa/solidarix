package com.solidarix.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
public class MessageDto {

    private Long id;
    private UserSimpleDto sender;
    private String content;
    private LocalDateTime createdAt;
    private boolean isReadByReceiver; // null si non lu

    // Flag pour les différents types de message
    private Boolean isAboutHelpOffer;
    private Boolean isAboutInvitation;
    private Boolean isFromFriend;
}
