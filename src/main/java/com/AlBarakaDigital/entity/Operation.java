package com.AlBarakaDigital.entity;

import com.AlBarakaDigital.enums.OperationStatus;
import com.AlBarakaDigital.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private OperationStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime validatedAt;
    private LocalDateTime executedAt;

    @ManyToOne
    private Account accountSource;

    @ManyToOne
    private Account accountDestination;

    @OneToOne(mappedBy = "operation", cascade = CascadeType.ALL)
    private Document document;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

