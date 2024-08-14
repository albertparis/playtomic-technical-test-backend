package com.playtomic.tests.wallet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private String pspReference;
    private String amount;
    private LocalDateTime timestamp;
    private String type;


    public int getAmount() {
        // return amount in minor units
        return new BigDecimal(amount).multiply(new BigDecimal("100")).intValue();
    }
}
