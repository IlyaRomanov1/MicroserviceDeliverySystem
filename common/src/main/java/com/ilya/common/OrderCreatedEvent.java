package com.ilya.common;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderCreatedEvent {

    private Long userId;
    private BigDecimal totalPrice;

}
