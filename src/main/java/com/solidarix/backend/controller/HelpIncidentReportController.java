package com.solidarix.backend.controller;

import com.solidarix.backend.dto.HelpFeedbackDto;
import com.solidarix.backend.dto.HelpIncidentReportCreationDto;
import com.solidarix.backend.dto.HelpIncidentReportDto;
import com.solidarix.backend.model.HelpIncidentReport;
import com.solidarix.backend.model.IncidentType;
import com.solidarix.backend.model.User;
import com.solidarix.backend.security.CustomUserDetails;
import com.solidarix.backend.service.HelpIncidentReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class HelpIncidentReportController {

    private final HelpIncidentReportService incidentReportService;

    public HelpIncidentReportController(HelpIncidentReportService incidentReportService) {
        this.incidentReportService = incidentReportService;
    }

    // Lister tous les feedbacks d'une HelpOffer
    @GetMapping("/offer/{helpOfferId}")
    public ResponseEntity<List<HelpIncidentReportDto>> getFeedbacksForOffer(
            @PathVariable Long helpOfferId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<HelpIncidentReportDto> reports = incidentReportService.getIncidentReportsForHelpOffer(helpOfferId, userDetails.getUser());
        return ResponseEntity.ok(reports);
    }

}
