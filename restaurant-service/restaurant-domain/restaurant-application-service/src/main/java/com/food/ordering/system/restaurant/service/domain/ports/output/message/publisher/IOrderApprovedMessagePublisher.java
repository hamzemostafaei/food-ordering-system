package com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;

public interface IOrderApprovedMessagePublisher extends IDomainEventPublisher<OrderApprovedEvent> {
}
