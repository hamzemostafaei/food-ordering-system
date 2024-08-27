package com.food.ordering.system.order.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderCommand {
    private final String customerId;
    private final String restaurantId;
    private final BigDecimal price;
    private final List<OrderItem> items;
    private final OrderAddress address;
}
