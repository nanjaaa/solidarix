package com.solidarix.backend.dto;

import com.solidarix.backend.model.HelpFeedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
public class HelpFeedbackDto {
    private Long id;
    private Long helpOfferId;
    private Long authorId;
    private String feedback;
    private LocalDateTime createdAt;

    // Constructeur pratique pour transformer l'entité en DTO
    public static HelpFeedbackDto fromEntity(HelpFeedback entity) {

        HelpFeedbackDto feedbackDto = new HelpFeedbackDto();
        feedbackDto.id = entity.getId();
        feedbackDto.helpOfferId = entity.getHelpOffer().getId();
        feedbackDto.authorId = entity.getAuthor().getId();
        feedbackDto.feedback = entity.getFeedback();
        feedbackDto.createdAt = entity.getCreatedAt();

        return feedbackDto;
    }

}

