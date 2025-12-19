package com.AlBarakaDigital.service.impl;

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
    public AccountResponseDTO getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));
        return accountMapper.toDto(account);
    }

    @Override
    public AccountResponseDTO getAccountByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Account account = user.getAccount();
        if (account == null) {
            throw new RuntimeException("Aucun compte associé à cet utilisateur");
        }

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
