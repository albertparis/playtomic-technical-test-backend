package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.model.TransactionType;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.Payment;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWallet() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.TEN);

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));

        Optional<Wallet> result = walletService.getWallet(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals(BigDecimal.TEN, result.get().getBalance());
    }

    @Test
    void testTopUpWallet() throws StripeServiceException {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.ZERO);

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        doAnswer(invocation -> new Payment("some-id")).when(stripeService).charge(anyString(), any(BigDecimal.class));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet result = walletService.topUpWallet(1L, "1234567890123456", BigDecimal.TEN);

        assertNotNull(result);
        assertEquals(BigDecimal.TEN, result.getBalance());
        assertEquals(1, result.getTransactions().size());
        assertEquals(BigDecimal.TEN, result.getTransactions().get(0).getAmount());
        assertEquals(TransactionType.TOP_UP, result.getTransactions().get(0).getType());
        assertEquals("some-id", result.getTransactions().get(0).getPspReference());

        verify(walletRepository).findById(1L);
        verify(walletRepository).save(any(Wallet.class));
        verify(stripeService).charge(anyString(), any(BigDecimal.class));
    }

    @Test
    void testTopUpWallet_WalletNotFound() throws StripeServiceException {
        when(walletRepository.findById(1L)).thenReturn(Optional.empty());
        doAnswer(invocation -> new Payment("some-id")).when(stripeService).charge(anyString(), any(BigDecimal.class));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> {
            Wallet walletToSave = invocation.getArgument(0);
            walletToSave.setId(1L);
            walletToSave.setBalance(BigDecimal.TEN);
            return walletToSave;
        });


        Wallet result = walletService.topUpWallet(1L, "1234567890123456", BigDecimal.TEN);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(BigDecimal.TEN, result.getBalance());
        assertEquals(1, result.getTransactions().size());
        assertEquals(BigDecimal.TEN, result.getTransactions().get(0).getAmount());
        assertEquals(TransactionType.TOP_UP, result.getTransactions().get(0).getType());
        assertEquals("some-id", result.getTransactions().get(0).getPspReference());

        verify(walletRepository).findById(1L);
        verify(walletRepository).save(any(Wallet.class));
        verify(stripeService).charge(anyString(), any(BigDecimal.class));
    }

    @Test
    void testTopUpWallet_StripeServiceException() throws StripeServiceException {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.ZERO);

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        doThrow(new StripeServiceException()).when(stripeService).charge(anyString(), any(BigDecimal.class));

        assertThrows(StripeServiceException.class, () -> walletService.topUpWallet(1L, "1234567890123456", BigDecimal.TEN));

        verify(walletRepository, never()).save(any(Wallet.class));
    }

}