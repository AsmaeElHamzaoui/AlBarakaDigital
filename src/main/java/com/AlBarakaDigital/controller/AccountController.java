package com.AlBarakaDigital.controller;

import com.AlBarakaDigital.dto.AccountRequestDTO;
import com.AlBarakaDigital.dto.AccountResponseDTO;
import com.AlBarakaDigital.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class AccountController {

    private final AccountService accountService;

    // Créer un compte bancaire pour le client connecté
    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccountForClient(
            @RequestBody AccountRequestDTO requestDTO,
            Authentication authentication
    ) {
        // Récupérer l'email de l'utilisateur connecté depuis le JWT
        String email = authentication.getName();

        AccountResponseDTO response = accountService.createAccountForAuthenticatedUser(requestDTO, email);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}