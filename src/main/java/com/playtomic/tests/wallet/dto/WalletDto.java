package com.playtomic.tests.wallet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class WalletDto {
    private Long id;
    private int balance;
    private TransactionDto[] transactions;

    public int getBalance() {
        // return balance in minor units
        return new BigDecimal(balance).multiply(new BigDecimal("100")).intValue();
    }
}
