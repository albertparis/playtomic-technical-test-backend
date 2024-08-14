package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.infrastructure.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.infrastructure.client.StripeService;
import com.playtomic.tests.wallet.infrastructure.exception.StripeServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

/**
 * This test is failing with the current implementation.
 *
 * How would you test this?
 */
public class StripeServiceTest {

    private StripeService s;
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        URI testUri = URI.create("http://how-would-you-test-me.localhost");
        s = new StripeService(testUri, testUri, new RestTemplateBuilder());
        s.setRestTemplate(restTemplate);
    }

    @Test
    public void testChargeException() {
        doThrow(new StripeAmountTooSmallException())
                .when(restTemplate).postForObject(any(URI.class), any(Object.class), any(Class.class));

        Assertions.assertThrows(StripeAmountTooSmallException.class, () ->
                s.charge("4242 4242 4242 4242", new BigDecimal(5)));
    }

    @Test
    public void testChargeOk() throws StripeServiceException {
        s.charge("4242 4242 4242 4242", new BigDecimal(15));
    }
}
