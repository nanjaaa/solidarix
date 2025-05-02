package com.solidarix.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HelpOfferMessageDto {

    @NotBlank
    private Long helpOfferId;

    @NotBlank(message = "Le contenu du message est obligatoire")
    private String message;

}
