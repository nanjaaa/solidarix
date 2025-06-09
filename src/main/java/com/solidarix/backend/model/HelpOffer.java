package com.solidarix.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Data
@Table(name = "help_offers")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class HelpOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "helpRequest_id")
    private  HelpRequest helpRequest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "helper_id")
    private User helper;

    private LocalDateTime createdAt;

    private LocalDateTime expirationReference;

    @Enumerated(EnumType.STRING)
    private HelpOfferStatus status;

    @Column(length = 1000)
    private String cancellationJustification;

    private LocalDateTime canceledAt;

    private LocalDateTime closedAt;

    @OneToMany(mappedBy = "helpOffer")
    private List<HelpIncidentReport> incidentReports;

    public boolean isExpired(){
        if(this.status == HelpOfferStatus.PROPOSED || this.status == HelpOfferStatus.VALIDATED_BY_REQUESTER){
            return Duration.between(expirationReference, LocalDateTime.now()).toHours() >= 24;
        }
        return this.status == HelpOfferStatus.EXPIRED;
    }

}
