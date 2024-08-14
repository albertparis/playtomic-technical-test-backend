package com.playtomic.tests.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.port.exception.PaymentServiceException;
import com.playtomic.tests.wallet.domain.model.Wallet;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ObjectMapper objectMapper = new ObjectMapper();
        walletController = new WalletController(walletService, objectMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    void testGetWallet() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.TEN);

        when(walletService.getWallet(1L)).thenReturn(Optional.of(wallet));

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    void testGetWallet_NotFound() throws Exception {
        when(walletService.getWallet(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testTopUpWallet() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.TEN);

        when(walletService.topUpWallet(anyLong(), any(String.class), any(BigDecimal.class))).thenReturn(wallet);

        mockMvc.perform(post("/1/top-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"credit_card\":\"4242424242424242\",\"amount\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    void testTopUpWallet_StripeServiceException() throws Exception {
        when(walletService.topUpWallet(anyLong(), any(String.class), any(BigDecimal.class)))
                .thenThrow(new PaymentServiceException());

        mockMvc.perform(post("/1/top-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"credit_card\":\"4242424242424242\",\"amount\":10}"))
                .andExpect(status().isUnprocessableEntity());
    }
}