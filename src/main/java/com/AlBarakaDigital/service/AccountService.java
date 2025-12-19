package com.AlBarakaDigital.service;

import com.AlBarakaDigital.dto.AccountResponseDTO;

import java.math.BigDecimal;

public interface AccountService {

    AccountResponseDTO getAccountByNumber(String accountNumber);

    AccountResponseDTO getAccountByUserId(Long userId);

    AccountResponseDTO credit(String accountNumber, BigDecimal amount);

    AccountResponseDTO debit(String accountNumber, BigDecimal amount);
}
