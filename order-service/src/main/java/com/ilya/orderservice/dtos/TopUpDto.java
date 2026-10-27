package com.ilya.orderservice.dtos;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TopUpDto {

    @Positive(message = "Введите положительную сумму")
    private BigDecimal amount;
}
