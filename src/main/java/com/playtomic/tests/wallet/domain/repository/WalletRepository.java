package com.playtomic.tests.wallet.domain.repository;

import com.playtomic.tests.wallet.domain.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}