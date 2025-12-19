package com.AlBarakaDigital.controller;

import com.AlBarakaDigital.dto.OperationRequestDTO;
import com.AlBarakaDigital.dto.OperationResponseDTO;
import com.AlBarakaDigital.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/operations")
@RequiredArgsConstructor
public class ClientOperationController {

    private final OperationService operationService;

    @PostMapping
    public OperationResponseDTO createOperation(
            @RequestBody OperationRequestDTO dto,
            Authentication authentication) {

        return operationService.createOperation(
                dto,
                authentication.getName()
        );
    }

    @GetMapping
    public List<OperationResponseDTO> getMyOperations(
            Authentication authentication) {

        return operationService.getClientOperations(
                authentication.getName()
        );
    }
}
