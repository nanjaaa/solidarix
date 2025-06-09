package com.solidarix.backend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AttachmentUploadDto {

    private MultipartFile file;

}
