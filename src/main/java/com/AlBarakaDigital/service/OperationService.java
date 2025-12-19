package com.AlBarakaDigital.service;

import com.AlBarakaDigital.dto.OperationRequestDTO;
import com.AlBarakaDigital.dto.OperationResponseDTO;

import java.util.List;

public interface OperationService {

    // CLIENT
    OperationResponseDTO createOperation(OperationRequestDTO dto, String clientEmail);
    List<OperationResponseDTO> getClientOperations(String clientEmail);

    // AGENT
    List<OperationResponseDTO> getPendingOperations();
    OperationResponseDTO approveOperation(Long operationId);
    OperationResponseDTO rejectOperation(Long operationId);
}
