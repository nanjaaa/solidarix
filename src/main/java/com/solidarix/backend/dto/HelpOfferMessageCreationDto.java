package com.solidarix.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HelpOfferMessageCreationDto {

    @NotNull(message = "L'identifiant de la proposition est requis")
    private Long helpOfferId;

    @NotBlank(message = "Le contenu du message est obligatoire")
    private String message;

}
