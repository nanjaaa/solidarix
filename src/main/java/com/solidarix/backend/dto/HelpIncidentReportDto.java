package com.solidarix.backend.dto;

import com.solidarix.backend.model.HelpIncidentReport;
import com.solidarix.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelpIncidentReportDto {

    private Long id;
    private HelpOfferDto helpOffer;
    private UserSimpleDto reporter;
    private String description;
    private LocalDateTime createdAt;

    // Ajouter les fichiers joints

    // Constructeur à partir de l'entité
    public static HelpIncidentReportDto fromEntity(HelpIncidentReport report, User reporter) {

        HelpIncidentReportDto reportDto = new HelpIncidentReportDto();
        reportDto.id = report.getId();
        reportDto.helpOffer = HelpOfferDto.fromHelpOfferEntityWithPrivateHelpRequest(report.getHelpOffer(), reporter);
        reportDto.reporter = UserSimpleDto.fromEntity(report.getReporter());
        reportDto.description = report.getDescription();
        reportDto.createdAt = report.getCreatedAt();

        return reportDto;
    }
}

