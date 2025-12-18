package com.AlBarakaDigital.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponseDTO {
    private String accountNumber;
    private BigDecimal balance;
}

