package com.solidarix.backend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class HelpIncidentReportCreationDto {

    @NotBlank(message = "La description de l'incident est obligatoire")
    @Size(max = 1000, message = "La description en doit pas dépasser 1000 caractères")
    private String description;

    @NotBlank(message = "La catégorie de l'incident doit être précisée")
    private String type;

}
