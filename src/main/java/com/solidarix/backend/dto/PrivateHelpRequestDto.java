package com.solidarix.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateHelpRequestDto extends PublicHelpRequestDto{

    private String fullAddress;

    public PrivateHelpRequestDto(
            Long id,
            UserSimpleDto user,
            String category,
            String description,
            LocalDateTime helpDate,
            LocalDateTime createdAt,
            String status,
            String postalSummary,
            List<HelpRequestCommentDto> comments,
            String fullAddress
    ) {
        super(id, user, category, description, helpDate, createdAt, status, postalSummary, comments);
        this.fullAddress = fullAddress;
    }


}
