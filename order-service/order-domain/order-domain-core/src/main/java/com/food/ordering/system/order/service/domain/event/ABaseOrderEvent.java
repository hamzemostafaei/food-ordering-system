package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.domain.event.IDomainEvent;
import com.food.ordering.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public abstract class ABaseOrderEvent implements IDomainEvent<Order> {

    private final Order order;
    private final ZonedDateTime createdAt;

    public ABaseOrderEvent(Order order, ZonedDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }

    public Order getOrder() {
        return order;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
