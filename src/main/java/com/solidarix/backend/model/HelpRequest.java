package com.solidarix.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "help_requests")
@Data
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class HelpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requester_id")
    private User requester;

    @Enumerated(EnumType.STRING)
    private HelpCategory category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id")
    private Location location;

    private LocalDateTime helpDate;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private HelpStatus status;

    @Column(length = 1000)
    private String description;

    //ajouter des commentaires plus tard

}
