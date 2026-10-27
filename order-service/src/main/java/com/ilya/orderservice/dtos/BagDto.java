package com.ilya.orderservice.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class BagDto {
    private Long id;
    private Long userId;
    private Long foodId;
    private String foodName;
    private BigDecimal foodPrice;
    @Positive(message = "Введите положительную сумму")
    private Long quantity;
}
