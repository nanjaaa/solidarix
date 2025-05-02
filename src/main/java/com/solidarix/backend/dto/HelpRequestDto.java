package com.solidarix.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HelpRequestDto {

    private String category;
    private String fullAddress;
    private LocalDateTime helpDate;
    private String description;

}
