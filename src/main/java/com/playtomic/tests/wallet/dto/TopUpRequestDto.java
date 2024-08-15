package com.playtomic.tests.wallet.dto;

import com.playtomic.tests.wallet.validation.ValidCreditCard;
import lombok.NonNull;

import java.math.BigDecimal;

public record TopUpRequestDto(@ValidCreditCard @NonNull String creditCard, @NonNull BigDecimal amount) {
}
