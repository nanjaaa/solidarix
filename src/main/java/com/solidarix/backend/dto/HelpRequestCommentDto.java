package com.solidarix.backend.dto;

import lombok.Data;

@Data
public class HelpRequestCommentDto {

    private Long helpRequestId;
    private Long parentCommentId;
    private String content;

}
