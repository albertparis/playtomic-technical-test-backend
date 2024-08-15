package com.playtomic.tests.wallet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(Long id, String pspReference, int amount, LocalDateTime timestamp, String type) {

    @Override
    public int amount() {
        // return amount in minor units
        return new BigDecimal(amount).multiply(new BigDecimal("100")).intValue();
    }
}