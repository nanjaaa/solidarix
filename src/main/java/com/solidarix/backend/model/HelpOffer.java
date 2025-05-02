package com.solidarix.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    private HelpOfferStatus status;

}
