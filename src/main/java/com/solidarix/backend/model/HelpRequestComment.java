package com.solidarix.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Data
@Table(name = "help_request_comments")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class HelpRequestComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "help_request_id")
    private HelpRequest helpRequest;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private HelpRequestComment parentComment;

    @Column(nullable = false, length = 1000)
    private String content;

    private LocalDateTime createdAt;

}
