package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.dto.TopUpRequest;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.service.StripeServiceException;
import com.playtomic.tests.wallet.service.WalletService;
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

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWallet(@PathVariable Long id) {
        Optional<Wallet> wallet = walletService.getWallet(id);
        return wallet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/top-up")
    public ResponseEntity<Wallet> topUpWallet(@PathVariable Long id, @Valid @RequestBody TopUpRequest topUpRequest) {
        try {
            Wallet wallet = walletService.topUpWallet(id, topUpRequest.getCreditCard(), topUpRequest.getAmount());
            return ResponseEntity.ok(wallet);
        } catch (StripeServiceException e) {
            return ResponseEntity.status(422).body(null);
        }
    }
}
