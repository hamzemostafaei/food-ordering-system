package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends ABaseOrderEvent {

    private final IDomainEventPublisher<OrderCancelledEvent> orderCancelledEventPublisher;

    public OrderCancelledEvent(Order order,
                               ZonedDateTime createdAt,
                               IDomainEventPublisher<OrderCancelledEvent> orderCancelledEventPublisher) {
        super(order, createdAt);
        this.orderCancelledEventPublisher = orderCancelledEventPublisher;
    }

    @Override
    public void fire() {
        orderCancelledEventPublisher.publish(this);
    }
}
