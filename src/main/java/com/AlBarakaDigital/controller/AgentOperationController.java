package com.AlBarakaDigital.controller;

import com.AlBarakaDigital.dto.OperationResponseDTO;
import com.AlBarakaDigital.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent/operations")
@RequiredArgsConstructor
public class AgentOperationController {

    private final OperationService operationService;

    @GetMapping("/pending")
    public List<OperationResponseDTO> getPendingOperations() {
        return operationService.getPendingOperations();
    }

}
