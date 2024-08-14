package com.playtomic.tests.wallet.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;

public record PaymentDto(@Getter @NonNull String id) {

    @JsonCreator
    public PaymentDto(@JsonProperty(value = "id", required = true) @NonNull String id) {
        this.id = id;
    }
}
