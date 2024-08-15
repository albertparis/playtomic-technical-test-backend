package com.playtomic.tests.wallet.dto;

import java.math.BigDecimal;

public record WalletDto(Long id, int balance, TransactionDto[] transactions) {
    @Override
    public int balance() {
        // return balance in minor units
        return new BigDecimal(balance).multiply(new BigDecimal("100")).intValue();
    }
}
