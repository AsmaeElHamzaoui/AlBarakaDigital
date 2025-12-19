package com.AlBarakaDigital.dto;

import com.AlBarakaDigital.enums.OperationStatus;
import com.AlBarakaDigital.enums.OperationType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OperationResponseDTO {
    private Long id;
    private OperationType type;
    private BigDecimal amount;
    private OperationStatus status;
    private LocalDateTime createdAt;
    private String accountSource;
    private String accountDestination;
}
