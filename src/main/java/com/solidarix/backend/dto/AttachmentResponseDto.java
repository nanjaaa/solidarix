package com.solidarix.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class AttachmentResponseDto {

    private String fileName;
    private String fileUrl;

}
