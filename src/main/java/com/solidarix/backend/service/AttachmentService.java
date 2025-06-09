package com.solidarix.backend.service;

import com.solidarix.backend.dto.AttachmentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    // Répertoire de stockage local - le chemin complet vers le dossier de destination à configurer dans application.properties
    @Value("${upload.directory:/uploads}")
    private String uploadDirectory;

    public AttachmentResponseDto uploadFile(MultipartFile file){

        File uploadDir = new File(System.getProperty("user.dir"), uploadDirectory);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }

        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDirectory, fileName);

        // Transfert du fichier vers le dossier local
        try{
            file.transferTo(filePath.toFile());
        }catch (IOException e){
            throw new RuntimeException("Erreur lors de l'upload du fichier : " + e.getMessage());
        }


        String fileUrl = uploadDirectory + fileName;

        // Ici, on ne sauvegarde pas l'entité, c'est au service spécifique de le faire (cas précis)
        return new AttachmentResponseDto(fileName, fileUrl);
    }

}
