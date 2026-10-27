package com.ilya.orderservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class WalletDto {
    private Long userId;
    private BigDecimal balance;
}
