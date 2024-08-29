package com.food.ordering.system.payment.service.domain.event;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCompletedEvent extends PaymentEvent {

    private final IDomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventPublisher;

    public PaymentCompletedEvent(Payment payment,
                                 ZonedDateTime createdAt,
                                 IDomainEventPublisher<PaymentCompletedEvent> domainEventPublisher) {
        super(payment, createdAt, Collections.emptyList());
        this.paymentCompletedEventPublisher = domainEventPublisher;
    }

    @Override
    public void fire() {
        this.paymentCompletedEventPublisher.publish(this);
    }
}
