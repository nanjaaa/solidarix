package com.solidarix.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HelpRequestCommentCreationDto {

    @NotBlank
    private Long helpRequestId;

    private Long parentCommentId;

    @NotBlank(message = "Le contenu est obligatoire")
    private String content;

}
