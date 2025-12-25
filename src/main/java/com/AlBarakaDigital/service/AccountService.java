package com.AlBarakaDigital.service;

import com.AlBarakaDigital.dto.AccountRequestDTO;
import com.AlBarakaDigital.dto.AccountResponseDTO;

import java.math.BigDecimal;

public interface AccountService {

    // Récupérer un compte par numéro
    AccountResponseDTO getAccountByNumber(String accountNumber);

    // Récupérer un compte par userId
    AccountResponseDTO getAccountByUserId(Long userId);

    // Créditer un compte
    AccountResponseDTO credit(String accountNumber, BigDecimal amount);

    // Débiter un compte
    AccountResponseDTO debit(String accountNumber, BigDecimal amount);

    // Créer un compte (request DTO + utilisateur authentifié)
    AccountResponseDTO createAccountForAuthenticatedUser(AccountRequestDTO accountRequestDTO, String userEmail);
}
