package com.AlBarakaDigital.service.impl;

import com.AlBarakaDigital.dto.OperationRequestDTO;
import com.AlBarakaDigital.dto.OperationResponseDTO;
import com.AlBarakaDigital.entity.Account;
import com.AlBarakaDigital.entity.Operation;
import com.AlBarakaDigital.enums.OperationStatus;
import com.AlBarakaDigital.enums.OperationType;
import com.AlBarakaDigital.mapper.OperationMapper;
import com.AlBarakaDigital.repository.AccountRepository;
import com.AlBarakaDigital.repository.OperationRepository;
import com.AlBarakaDigital.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OperationServiceImpl implements OperationService {

    private static final BigDecimal AUTO_VALIDATION_LIMIT = new BigDecimal("10000");

    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;
    private final OperationMapper operationMapper;

    // ================= CLIENT =================

    @Override
    public OperationResponseDTO createOperation(OperationRequestDTO dto, String clientEmail) {

        // Récupérer le compte source
        Account sourceAccount = accountRepository.findAll()
                .stream()
                .filter(a -> a.getOwner().getEmail().equals(clientEmail))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Compte client introuvable"));

        // Mapper DTO → Entity (sans les accounts pour l’instant)
        Operation operation = operationMapper.toEntity(dto);
        operation.setAccountSource(sourceAccount);

        // Si c'est un transfert, récupérer le compte destination
        if (dto.getType() == OperationType.TRANSFER) {
            Account destination = accountRepository
                    .findByAccountNumber(dto.getDestinationAccountNumber())
                    .orElseThrow(() -> new RuntimeException("Compte destination introuvable"));
            operation.setAccountDestination(destination);
        }

        // Auto-validation si montant <= 10000
        boolean autoValidate = dto.getAmount().compareTo(AUTO_VALIDATION_LIMIT) <= 0;
        if (autoValidate) {
            executeOperation(operation);
            operation.setStatus(OperationStatus.APPROVE);
            operation.setValidatedAt(LocalDateTime.now());
            operation.setExecutedAt(LocalDateTime.now());
        } else {
            operation.setStatus(OperationStatus.PENDING);
        }

        // Sauvegarder et mapper Entity → DTO
        return operationMapper.toDto(operationRepository.save(operation));
    }

    @Override
    public List<OperationResponseDTO> getClientOperations(String clientEmail) {
        return operationRepository.findByAccountSource_Owner_Email(clientEmail)
                .stream()
                .map(operationMapper::toDto)
                .toList();
    }

    // ================= AGENT =================

    @Override
    public List<OperationResponseDTO> getPendingOperations() {
        return operationRepository.findByStatus(OperationStatus.PENDING)
                .stream()
                .map(operationMapper::toDto)
                .toList();
    }

    @Override
    public OperationResponseDTO approveOperation(Long operationId) {
        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Opération introuvable"));

        if (operation.getStatus() != OperationStatus.PENDING) {
            throw new RuntimeException("Opération déjà traitée");
        }

        executeOperation(operation);
        operation.setStatus(OperationStatus.APPROVE);
        operation.setValidatedAt(LocalDateTime.now());
        operation.setExecutedAt(LocalDateTime.now());

        return operationMapper.toDto(operationRepository.save(operation));
    }

    @Override
    public OperationResponseDTO rejectOperation(Long operationId) {
        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Opération introuvable"));

        if (operation.getStatus() != OperationStatus.PENDING) {
            throw new RuntimeException("Opération déjà traitée");
        }

        operation.setStatus(OperationStatus.REJECT);
        operation.setValidatedAt(LocalDateTime.now());

        return operationMapper.toDto(operationRepository.save(operation));
    }

    // ================= LOGIQUE MÉTIER =================

    private void executeOperation(Operation operation) {
        Account source = operation.getAccountSource();
        BigDecimal amount = operation.getAmount();

        switch (operation.getType()) {
            case DEPOSIT -> source.setBalance(source.getBalance().add(amount));

            case WITHDRAWAL -> {
                if (source.getBalance().compareTo(amount) < 0) {
                    throw new RuntimeException("Solde insuffisant");
                }
                source.setBalance(source.getBalance().subtract(amount));
            }

            case TRANSFER -> {
                Account destination = operation.getAccountDestination();
                if (source.getBalance().compareTo(amount) < 0) {
                    throw new RuntimeException("Solde insuffisant");
                }
                source.setBalance(source.getBalance().subtract(amount));
                destination.setBalance(destination.getBalance().add(amount));
            }
        }
    }
}
