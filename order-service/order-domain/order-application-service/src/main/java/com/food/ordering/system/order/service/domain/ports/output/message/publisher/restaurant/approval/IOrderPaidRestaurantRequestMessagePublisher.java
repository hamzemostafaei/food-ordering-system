package com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurant.approval;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

public interface IOrderPaidRestaurantRequestMessagePublisher extends IDomainEventPublisher<OrderPaidEvent> {
}
