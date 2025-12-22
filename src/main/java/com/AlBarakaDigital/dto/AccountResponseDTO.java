package com.AlBarakaDigital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;

    // Owner du compte
    private UserResponseDTO owner;

    // Opérations sortantes
    private List<OperationResponseDTO> outgoingOperations;

    // Opérations entrantes
    private List<OperationResponseDTO> incomingOperations;
}
