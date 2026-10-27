package com.ilya.orderservice.services;

import com.ilya.orderservice.models.Wallet;
import com.ilya.orderservice.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletService {

    private final WalletRepository walletRepository;

    public BigDecimal getBalance(Long userId) {
        return walletRepository.findById(userId)
                .map(Wallet::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    @Transactional
    public BigDecimal topUp(Long userId, BigDecimal amount) {
        Wallet wallet = getOrCreate(userId);
        wallet.setBalance(wallet.getBalance().add(amount));
        return walletRepository.save(wallet).getBalance();
    }

    /**
     * Списывает сумму с баланса, если средств достаточно.
     * @return true если списание прошло успешно, false если средств не хватает
     */
    @Transactional
    public boolean charge(Long userId, BigDecimal amount) {
        Wallet wallet = getOrCreate(userId);
        if (wallet.getBalance().compareTo(amount) < 0) {
            return false;
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
        return true;
    }

    private Wallet getOrCreate(Long userId) {
        return walletRepository.findById(userId)
                .orElseGet(() -> new Wallet(userId, BigDecimal.ZERO));
    }
}
