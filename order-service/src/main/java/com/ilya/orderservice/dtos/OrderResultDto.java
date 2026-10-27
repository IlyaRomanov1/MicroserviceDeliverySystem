package com.ilya.orderservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class OrderResultDto {
    private String status;
    private BigDecimal totalPrice;
    private BigDecimal balance;
    private String message;
}
