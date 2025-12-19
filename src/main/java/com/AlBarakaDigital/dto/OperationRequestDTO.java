package com.AlBarakaDigital.dto;

import com.AlBarakaDigital.enums.OperationType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperationRequestDTO {
    private OperationType type;
    private BigDecimal amount;
    private String destinationAccountNumber; // pour virement
}
