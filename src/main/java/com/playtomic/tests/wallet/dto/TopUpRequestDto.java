package com.playtomic.tests.wallet.dto;

import com.playtomic.tests.wallet.validation.ValidCreditCard;
import lombok.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record TopUpRequestDto(@ValidCreditCard @NonNull String creditCard, int amount) {
    public BigDecimal getAmountFromMinorUnits() {
        return BigDecimal.valueOf(amount).divide(new BigDecimal("100"), RoundingMode.HALF_UP);
    }
}
