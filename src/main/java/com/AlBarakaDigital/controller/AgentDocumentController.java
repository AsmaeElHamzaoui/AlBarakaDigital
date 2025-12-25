package com.AlBarakaDigital.controller;

import com.AlBarakaDigital.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent/operations")
@RequiredArgsConstructor
public class AgentDocumentController {

    private final DocumentService documentService;

    @GetMapping("/{operationId}/document")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long operationId
    ) {

        Resource resource = documentService.downloadDocument(operationId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF) // ou MediaType.APPLICATION_OCTET_STREAM
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);
    }
}
