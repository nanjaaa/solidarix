package com.solidarix.backend.controller;

import com.solidarix.backend.dto.HelpIncidentReportDto;
import com.solidarix.backend.model.HelpIncidentReport;
import com.solidarix.backend.model.User;
import com.solidarix.backend.security.CustomUserDetails;
import com.solidarix.backend.service.HelpIncidentReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report/{offerId}")
public class HelpIncidentReportController {

    private final HelpIncidentReportService incidentReportService;

    public HelpIncidentReportController(HelpIncidentReportService incidentReportService) {
        this.incidentReportService = incidentReportService;
    }

    @PostMapping("/create")
    public ResponseEntity<HelpIncidentReport> createIncdentReport(
            @PathVariable Long offerId
            , @AuthenticationPrincipal CustomUserDetails userDetails
            , @Valid @ModelAttribute HelpIncidentReportDto incidentReportDto) {

        User reporter = userDetails.getUser();
        HelpIncidentReport incidentReport = incidentReportService.createIncidentHelpReport(reporter, offerId, incidentReportDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(incidentReport);
    }

}
