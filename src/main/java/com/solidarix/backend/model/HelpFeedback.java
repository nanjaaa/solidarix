package com.solidarix.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "help_feedbacks")
@Data
@AllArgsConstructor @NoArgsConstructor
public class HelpFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "help_offer_id")
    private HelpOffer helpOffer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(length = 1000)
    private String feedback;

    private LocalDateTime createdAt;
}

