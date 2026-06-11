package com.ilya.notificationservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderViewDto {
    private Long userId;
    private BigDecimal totalPrice;
}
