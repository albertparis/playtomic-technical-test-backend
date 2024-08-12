package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.model.TransactionType;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final StripeService stripeService;

    public Optional<Wallet> getWallet(Long id) {
        return walletRepository.findById(id);
    }

    @Transactional
    public Wallet topUpWallet(Long id, String creditCardNumber, BigDecimal amount) throws StripeServiceException {
        Wallet wallet = walletRepository.findById(id).orElseGet(() -> {
            Wallet newWallet = new Wallet();
            newWallet.setId(id);
            newWallet.setBalance(BigDecimal.ZERO);
            return newWallet;
        });
        Payment response = stripeService.charge(creditCardNumber, amount);
        wallet.setBalance(wallet.getBalance().add(amount));

        // Add transaction using Wallet's method
        wallet.addTransaction(amount, TransactionType.TOP_UP, response.id());

        return walletRepository.save(wallet);
    }
}