package com.solidarix.backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HelpRequestCreationDto {

    @NotBlank(message = "La catégorie est obligatoire")
    private String category;

    @NotBlank(message = "L'adresse est obligatoire")
    private LocationDto address;

    @NotBlank(message = "La date est obligatoire")
    @Future(message = "La date de l'aide doit être ultérieure")
    private LocalDateTime helpDate;

    @NotBlank(message = "La description est obligatoire")
    private String description;

}
