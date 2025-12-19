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


    @PutMapping("/{id}/approve")
    public OperationResponseDTO approve(@PathVariable Long id) {
        return operationService.approveOperation(id);
    }

    @PutMapping("/{id}/reject")
    public OperationResponseDTO reject(@PathVariable Long id) {
        return operationService.rejectOperation(id);
    }
}
