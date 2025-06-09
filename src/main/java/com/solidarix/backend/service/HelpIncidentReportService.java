package com.solidarix.backend.service;

import com.solidarix.backend.dto.AttachmentResponseDto;
import com.solidarix.backend.dto.HelpIncidentReportDto;
import com.solidarix.backend.model.*;
import com.solidarix.backend.repository.HelpIncidentReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class HelpIncidentReportService {

    private final AttachmentService attachmentService;
    private final HelpOfferService helpOfferService;
    private final HelpIncidentReportRepository incidentReportRepository;

    public HelpIncidentReportService(AttachmentService attachmentService, HelpOfferService helpOfferService, HelpIncidentReportRepository incidentReportRepository) {
        this.attachmentService = attachmentService;
        this.helpOfferService = helpOfferService;
        this.incidentReportRepository = incidentReportRepository;
    }

    // Vérification si celui qui crée le rapport d'incident est éligible pour faire le rapport qu'il formule selon les types d'incidents
    public boolean isHelpIncidentReportConsistent(User reporter, HelpOffer helpOffer, IncidentType incidentType){

        // Un proposeur d'aide ne peut pas réclamer que lui il n'a pas achevé sa mission en tant qu'aideur
        if(!reporter.equals(helpOffer.getHelpRequest().getRequester())
            && incidentType.equals(IncidentType.INCOMPLETE_HELP)){
            return false;
        } else if (false) {
            // Ajouter des cas ici au fur et à mesure qu'on en trouve
        }


        return true;
    }


    public HelpIncidentReport createIncidentHelpReport(User reporter, Long helpOfferId, HelpIncidentReportDto reportDto){

        HelpOffer helpOffer = helpOfferService.findById(helpOfferId);

        if (!isHelpIncidentReportConsistent(reporter, helpOffer, IncidentType.valueOf(reportDto.getCategory()))){
            throw new RuntimeException("Illegal incident type for the reporter.");
        }

        HelpIncidentReport report = new HelpIncidentReport();
        report.setReporter(reporter);
        report.setHelpOffer(helpOffer);
        report.setType(IncidentType.valueOf(reportDto.getCategory()));
        report.setDescription(reportDto.getDescription());
        report.setCreatedAt(LocalDateTime.now());

        for(MultipartFile file : reportDto.getAttachments()){
            AttachmentResponseDto response = attachmentService.uploadFile(file);

            IncidentReportAttachment attachment = new IncidentReportAttachment();
            attachment.setFileName(response.getFileName());
            attachment.setFileUrl(response.getFileUrl());
            attachment.setUploadAt(LocalDateTime.now());
            attachment.setIncidentReport(report);

            report.getAttachments().add(attachment);
        }

        helpOfferService.markAsFailed(reporter, helpOfferId);

        return incidentReportRepository.save(report); // Les entités IncidentReportAttachment sont sauvegardé automatiquement dans la base grâce au CASCADE

    }

}
