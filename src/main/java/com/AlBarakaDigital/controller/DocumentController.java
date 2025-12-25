package com.AlBarakaDigital.controller;

import com.AlBarakaDigital.dto.DocumentResponseDTO;
import com.AlBarakaDigital.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/client/operations")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/{operationId}/document")
    public DocumentResponseDTO uploadDocument(
            @PathVariable Long operationId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {
        return documentService.uploadDocument(
                operationId,
                file,
                authentication.getName()
        );
    }
}
