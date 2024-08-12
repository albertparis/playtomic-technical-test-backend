package com.playtomic.tests.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.playtomic.tests.wallet.validation.ValidCreditCard;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TopUpRequest {
    @NonNull
    @JsonProperty("credit_card")
    @ValidCreditCard
    private String creditCard;

    @NonNull
    @JsonProperty("amount")
    private BigDecimal amount;
}
