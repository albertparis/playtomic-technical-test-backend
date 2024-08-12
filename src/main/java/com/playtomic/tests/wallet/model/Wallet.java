package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal balance;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(BigDecimal amount, TransactionType type, String pspReference) {
        Transaction transaction = new Transaction();
        transaction.setPspReference(pspReference);
        transaction.setWallet(this);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(type);
        this.transactions.add(transaction);
    }
}