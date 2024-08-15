package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.port.exception.PaymentServiceException;
import com.playtomic.tests.wallet.domain.model.TransactionType;
import com.playtomic.tests.wallet.domain.model.Wallet;
import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import com.playtomic.tests.wallet.dto.PaymentDto;
import com.playtomic.tests.wallet.port.PaymentService;
import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final PaymentService paymentService;

    public Optional<Wallet> getWallet(Long id) {
        return walletRepository.findById(id);
    }

    @Transactional
    public Wallet topUpWallet(Long id, String creditCardNumber, BigDecimal amount) throws PaymentServiceException, OptimisticLockException {
        Wallet wallet = walletRepository.findById(id).orElseGet(() -> {
            Wallet newWallet = new Wallet();
            newWallet.setId(id);
            newWallet.setBalance(BigDecimal.ZERO);
            return newWallet;
        });
        PaymentDto response = paymentService.charge(creditCardNumber, amount);
        wallet.setBalance(wallet.getBalance().add(amount));

        // Add transaction using Wallet's method
        wallet.addTransaction(amount, TransactionType.TOP_UP, response.id());

        return walletRepository.save(wallet);
    }
}