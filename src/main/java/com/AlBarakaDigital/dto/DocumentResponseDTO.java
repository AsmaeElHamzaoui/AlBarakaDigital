package com.AlBarakaDigital.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentResponseDTO {
    private String fileName;
    private String fileType;
    private LocalDateTime uploadedAt;
}

