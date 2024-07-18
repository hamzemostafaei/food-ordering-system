package com.food.ordering.system.domain.event.publisher;

import com.food.ordering.system.domain.event.IDomainEvent;

public interface IDomainEventPublisher<T extends IDomainEvent<?>> {

    void publish(T domainEvent);
}
