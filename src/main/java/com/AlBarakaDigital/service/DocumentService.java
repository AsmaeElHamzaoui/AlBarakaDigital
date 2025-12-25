package com.AlBarakaDigital.service;

import com.AlBarakaDigital.dto.DocumentResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    DocumentResponseDTO uploadDocument(
            Long operationId,
            MultipartFile file,
            String clientEmail
    );
}
