package com.solidarix.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HelpOfferCancellationDto {

    @NotBlank(message = "Veuillez préciser une raison pour l'annulation.")
    private String justification;

}
