package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @JsonBackReference
    private Wallet wallet;

    @Column(unique = true)
    private String pspReference;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private TransactionType type; // e.g., "top-up", "spend", "refund"
}