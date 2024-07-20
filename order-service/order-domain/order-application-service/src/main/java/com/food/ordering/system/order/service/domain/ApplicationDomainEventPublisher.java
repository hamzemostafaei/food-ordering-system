package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationDomainEventPublisher implements ApplicationEventPublisherAware,
                                                        IDomainEventPublisher<OrderCreatedEvent> {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    @SuppressWarnings("NullableProblems")
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        this.applicationEventPublisher.publishEvent(domainEvent);
        log.info("OrderCreatedEvent published for order {}", domainEvent);
    }
}
