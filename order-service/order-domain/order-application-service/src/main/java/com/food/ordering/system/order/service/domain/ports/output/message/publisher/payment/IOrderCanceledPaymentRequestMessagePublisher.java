package com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderCanceledEvent;

public interface IOrderCanceledPaymentRequestMessagePublisher extends IDomainEventPublisher<OrderCanceledEvent> {
}
