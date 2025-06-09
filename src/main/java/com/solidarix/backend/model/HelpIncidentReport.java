package com.solidarix.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name = "help_incident_reports")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class HelpIncidentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "help_offer_id")
    private HelpOffer helpOffer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @Enumerated(EnumType.STRING)
    private IncidentType type;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "incidentReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncidentReportAttachment> attachments;

    private LocalDateTime createdAt;


}
