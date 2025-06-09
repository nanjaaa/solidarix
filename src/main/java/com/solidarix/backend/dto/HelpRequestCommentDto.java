package com.solidarix.backend.dto;

import com.solidarix.backend.model.HelpRequestComment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class HelpRequestCommentDto {

    private Long id;
    private UserSimpleDto author;
    private String content;
    private LocalDateTime createdAt;
    private List<HelpRequestCommentDto> replies;

    public static HelpRequestCommentDto fromEntity(HelpRequestComment c, List<HelpRequestCommentDto> replies) {
        return new HelpRequestCommentDto(
                c.getId(),
                UserSimpleDto.fromEntity(c.getAuthor()),
                c.getContent(),
                c.getCreatedAt(),
                replies
        );
    }
}

