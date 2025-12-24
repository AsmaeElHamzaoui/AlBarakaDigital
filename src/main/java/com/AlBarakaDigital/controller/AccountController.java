package com.AlBarakaDigital.controller;

import com.AlBarakaDigital.dto.AccountRequestDTO;
import com.AlBarakaDigital.dto.AccountResponseDTO;
import com.AlBarakaDigital.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AccountController {

    private final AccountService accountService;

    // Créer un compte bancaire pour un client
    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccountForClient(
            @RequestParam Long userId,
            @RequestBody AccountRequestDTO requestDTO
    ) {
        AccountResponseDTO response =
                accountService.createAccount(requestDTO, userId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
