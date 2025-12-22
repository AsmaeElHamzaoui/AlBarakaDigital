package com.AlBarakaDigital.service.impl;

import com.AlBarakaDigital.dto.AccountRequestDTO;
import com.AlBarakaDigital.dto.AccountResponseDTO;
import com.AlBarakaDigital.entity.Account;
import com.AlBarakaDigital.entity.User;
import com.AlBarakaDigital.mapper.AccountMapper;
import com.AlBarakaDigital.repository.AccountRepository;
import com.AlBarakaDigital.repository.UserRepository;
import com.AlBarakaDigital.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO, Long userId) {
        // Récupérer le user depuis l'id (JWT)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Vérifier si le numéro de compte existe déjà
        if (accountRepository.existsByAccountNumber(accountRequestDTO.getAccountNumber())) {
            throw new RuntimeException("Numéro de compte déjà utilisé");
        }

        // Mapper le DTO vers l'entité
        Account account = accountMapper.toEntity(accountRequestDTO);
        account.setOwner(user);

        // Sauvegarder
        Account savedAccount = accountRepository.save(account);

        // Retourner le DTO de réponse
        return accountMapper.toDto(savedAccount);
    }

    @Override
    public AccountResponseDTO getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));
        return accountMapper.toDto(account);
    }

    @Override
    public AccountResponseDTO getAccountByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Récupérer le compte associé à l'utilisateur
        Account account = accountRepository.findById(user.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Aucun compte associé à cet utilisateur"));

        return accountMapper.toDto(account);
    }

    @Override
    public AccountResponseDTO credit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        account.setBalance(account.getBalance().add(amount));
        return accountMapper.toDto(accountRepository.save(account));
    }

    @Override
    public AccountResponseDTO debit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Solde insuffisant");
        }

        account.setBalance(account.getBalance().subtract(amount));
        return accountMapper.toDto(accountRepository.save(account));
    }
}
