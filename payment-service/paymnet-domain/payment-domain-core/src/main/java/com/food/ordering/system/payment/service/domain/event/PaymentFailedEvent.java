package com.food.ordering.system.payment.service.domain.event;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFailedEvent extends PaymentEvent {

    private final IDomainEventPublisher<PaymentFailedEvent> paymentFailedEventPublisher;

    public PaymentFailedEvent(Payment payment,
                              ZonedDateTime createdAt,
                              List<String> failureMessages,
                              IDomainEventPublisher<PaymentFailedEvent> paymentFailedEventPublisher) {
        super(payment, createdAt, failureMessages);
        this.paymentFailedEventPublisher = paymentFailedEventPublisher;
    }

    @Override
    public void fire() {
        paymentFailedEventPublisher.publish(this);
    }
}
