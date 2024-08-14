package com.playtomic.tests.wallet.port;

import com.playtomic.tests.wallet.dto.PaymentDto;

import java.math.BigDecimal;

public interface PaymentService {
    PaymentDto charge(String creditCardNumber, BigDecimal amount);
    void refund(String paymentId);
}