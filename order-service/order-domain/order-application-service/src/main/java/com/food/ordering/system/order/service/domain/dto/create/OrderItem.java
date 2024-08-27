package com.food.ordering.system.order.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class OrderItem {
    private final String productId;
    private final Integer quantity;
    private final BigDecimal price;
    private final BigDecimal subTotal;
}
