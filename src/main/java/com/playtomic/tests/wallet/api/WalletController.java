package com.playtomic.tests.wallet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.dto.TopUpRequestDto;
import com.playtomic.tests.wallet.dto.WalletDto;
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
    public ResponseEntity<WalletDto> topUpWallet(@PathVariable Long id, @Valid @RequestBody TopUpRequestDto topUpRequestDto) {
        try {
            Wallet wallet = walletService.topUpWallet(id, topUpRequestDto.getCreditCard(), topUpRequestDto.getAmount());
            return ResponseEntity.ok(objectMapper.convertValue(wallet, WalletDto.class));
        } catch (StripeServiceException e) {
            return ResponseEntity.status(422).body(null);
        }
    }
}