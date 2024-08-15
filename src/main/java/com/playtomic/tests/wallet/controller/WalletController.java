package com.playtomic.tests.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.infrastructure.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.port.exception.PaymentServiceException;
import com.playtomic.tests.wallet.dto.TopUpRequestDto;
import com.playtomic.tests.wallet.dto.WalletDto;
import com.playtomic.tests.wallet.domain.model.Wallet;
import com.playtomic.tests.wallet.service.WalletService;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class WalletController {
    private final Logger log = LoggerFactory.getLogger(WalletController.class);

    private final WalletService walletService;
    private final ObjectMapper objectMapper;

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDto> getWallet(@PathVariable Long id) {
        Optional<Wallet> wallet = walletService.getWallet(id);
        wallet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        return wallet.map(w -> ResponseEntity.ok(objectMapper.convertValue(w, WalletDto.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/top-up")
    public ResponseEntity<?> topUpWallet(@PathVariable Long id, @Valid @RequestBody TopUpRequestDto topUpRequestDto) {
        try {
            Wallet wallet = walletService.topUpWallet(id, topUpRequestDto.creditCard(), topUpRequestDto.getAmountFromMinorUnits());
            return ResponseEntity.ok(objectMapper.convertValue(wallet, WalletDto.class));
        } catch (StripeAmountTooSmallException e) {
            return ResponseEntity.status(422).body("Amount is too small");
        } catch (PaymentServiceException e) {
            return ResponseEntity.status(422).body("Payment service error");
        } catch (OptimisticLockException e) {
            return ResponseEntity.status(409).body("Too many requests");
        }
    }
}
