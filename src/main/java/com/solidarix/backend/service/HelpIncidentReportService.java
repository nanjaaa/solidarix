package com.solidarix.backend.service;

import com.solidarix.backend.dto.HelpFeedbackCreationDto;
import com.solidarix.backend.dto.HelpFeedbackDto;
import com.solidarix.backend.dto.HelpIncidentReportDto;
import com.solidarix.backend.model.*;
import com.solidarix.backend.repository.HelpIncidentReportRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HelpIncidentReportService {

    private final HelpIncidentReportRepository incidentReportRepository;

    public HelpIncidentReportService(HelpOfferService helpOfferService, HelpIncidentReportRepository incidentReportRepository) {
        this.incidentReportRepository = incidentReportRepository;
    }


    public List<HelpIncidentReportDto> getIncidentReportsForHelpOffer(Long helpOfferId, User currentUser) {
        List<HelpIncidentReport> reports = incidentReportRepository.findByHelpOfferId(helpOfferId);
        return reports.stream()
                .map(report -> HelpIncidentReportDto.fromEntity(report, currentUser))
                .toList();
    }

    public HelpIncidentReportDto getHelpIncidentReportById(Long helpIncidentReportId, User currentUser){
        HelpIncidentReport report = incidentReportRepository.findById(helpIncidentReportId)
                .orElseThrow(() ->  new RuntimeException("Incident report not found!"));
        return HelpIncidentReportDto.fromEntity(report, currentUser);
    }

}