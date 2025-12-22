package com.AlBarakaDigital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String accountNumber;

    private BigDecimal balance = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    //  Opérations sortantes
    @OneToMany(mappedBy = "accountSource")
    private List<Operation> outgoingOperations;

    //  Opérations entrantes
    @OneToMany(mappedBy = "accountDestination")
    private List<Operation> incomingOperations;
}
