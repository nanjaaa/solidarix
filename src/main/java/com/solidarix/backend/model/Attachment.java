package com.solidarix.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public abstract class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileName;

    private String fileUrl;

    private LocalDateTime uploadAt;

}
