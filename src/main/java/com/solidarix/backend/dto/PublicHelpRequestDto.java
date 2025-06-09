package com.solidarix.backend.dto;

import com.solidarix.backend.model.HelpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicHelpRequestDto {
    private Long id;
    private UserSimpleDto requester;
    private String category;
    private String description;
    private LocalDateTime helpDate;
    private LocalDateTime createdAt;
    private String status;
    private String postalSummary; // Remplace l'adresse complète
    private List<HelpRequestCommentDto> comments;

    public static PublicHelpRequestDto fromEntity(HelpRequest h) {
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

        return new PublicHelpRequestDto(
                h.getId(),
                UserSimpleDto.fromEntity(h.getRequester()),
                h.getCategory().name(),
                h.getDescription(),
                h.getHelpDate(),
                h.getCreatedAt(),
                h.getStatus().name(),
                h.getLocation().getCity() + " " + h.getLocation().getPostalCode(),
                structuredComments
        );
    }

}
