package com.AlBarakaDigital.service.impl;

import com.AlBarakaDigital.dto.DocumentResponseDTO;
import com.AlBarakaDigital.entity.Document;
import com.AlBarakaDigital.entity.Operation;
import com.AlBarakaDigital.repository.DocumentRepository;
import com.AlBarakaDigital.repository.OperationRepository;
import com.AlBarakaDigital.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final OperationRepository operationRepository;

    private final String uploadDir = "uploads/documents/";

    @Override
    public DocumentResponseDTO uploadDocument(
            Long operationId,
            MultipartFile file,
            String clientEmail
    ) {

        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Operation introuvable"));

        // sécurité : vérifier que l’opération appartient au client
        if (!operation.getAccountSource().getOwner().getEmail().equals(clientEmail)) {
            throw new RuntimeException("Accès refusé");
        }

        try {
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            Files.write(filePath, file.getBytes());

            Document document = new Document();
            document.setFileName(fileName);
            document.setFileType(file.getContentType());
            document.setStoragePath(filePath.toString());
            document.setOperation(operation);

            documentRepository.save(document);

            operation.setDocument(document);

            DocumentResponseDTO dto = new DocumentResponseDTO();
            dto.setFileName(document.getFileName());
            dto.setFileType(document.getFileType());
            dto.setUploadedAt(document.getUploadedAt());

            return dto;

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l’upload du fichier");
        }
    }


    // ===================== DOWNLOAD (AGENT) =====================
    @Override
    public Resource downloadDocument(Long operationId) {

        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Operation introuvable"));

        Document document = operation.getDocument();
        if (document == null) {
            throw new RuntimeException("Aucun justificatif pour cette opération");
        }

        try {
            Path filePath = Paths.get(document.getStoragePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("Fichier introuvable sur le disque");
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Erreur lors du téléchargement");
        }
    }
}
