package com.ilya.orderservice.controllers;

import com.ilya.orderservice.dtos.TopUpDto;
import com.ilya.orderservice.dtos.WalletDto;
import com.ilya.orderservice.security.MyUserDetails;
import com.ilya.orderservice.services.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public WalletDto getBalance(@AuthenticationPrincipal MyUserDetails userDetails) {
        return new WalletDto(userDetails.getId(), walletService.getBalance(userDetails.getId()));
    }

    @PostMapping("/topup")
    public WalletDto topUp(@AuthenticationPrincipal MyUserDetails userDetails, @RequestBody @Valid TopUpDto dto) {
        var balance = walletService.topUp(userDetails.getId(), dto.getAmount());
        return new WalletDto(userDetails.getId(), balance);
    }
}
