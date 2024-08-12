package com.playtomic.tests.wallet.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;

public record Payment(@Getter @NonNull String id) {

    @JsonCreator
    public Payment(@JsonProperty(value = "id", required = true) @NonNull String id) {
        this.id = id;
    }
}
