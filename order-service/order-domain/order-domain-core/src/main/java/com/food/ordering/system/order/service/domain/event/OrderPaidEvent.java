package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends ABaseOrderEvent {

    private final IDomainEventPublisher<OrderPaidEvent> orderPaidEventPublisher;

    public OrderPaidEvent(Order order,
                          ZonedDateTime createdAt,
                          IDomainEventPublisher<OrderPaidEvent> orderPaidEventPublisher) {

        super(order, createdAt);
        this.orderPaidEventPublisher = orderPaidEventPublisher;
    }

    @Override
    public void fire() {
        orderPaidEventPublisher.publish(this);
    }
}
