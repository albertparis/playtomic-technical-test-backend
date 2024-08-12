package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}