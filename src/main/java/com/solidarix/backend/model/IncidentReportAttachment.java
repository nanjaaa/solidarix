package com.solidarix.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "incident_report_attachments")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class IncidentReportAttachment extends Attachment{

    @ManyToOne(optional = false)
    @JoinColumn(name = "report_id")
    private HelpIncidentReport incidentReport;

}
