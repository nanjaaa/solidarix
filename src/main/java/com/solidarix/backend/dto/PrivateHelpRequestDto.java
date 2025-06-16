package com.solidarix.backend.dto;

import com.solidarix.backend.model.HelpRequest;
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

    public static PrivateHelpRequestDto fromEntity(HelpRequest h) {
        List<HelpRequestCommentDto> structuredComments = h.getComments().stream()
                .filter(c -> c.getParentComment() == null)
                .map(c -> HelpRequestCommentDto.fromEntity(
                        c,
                        h.getComments().stream()
                                .filter(r -> r.getParentComment() != null && r.getParentComment().getId().equals(c.getId()))
                                .map(r -> HelpRequestCommentDto.fromEntity(r, List.of()))
                                .toList()
                ))
                .toList();

        return new PrivateHelpRequestDto(
                h.getId(),
                UserSimpleDto.fromEntity(h.getRequester()),
                h.getCategory().name(),
                h.getDescription(),
                h.getHelpDate(),
                h.getCreatedAt(),
                h.getStatus().name(),
                h.getLocation().getCity() + " " + h.getLocation().getPostalCode(),
                structuredComments,
                h.getLocation().getFullAddress()
        );
    }


}
