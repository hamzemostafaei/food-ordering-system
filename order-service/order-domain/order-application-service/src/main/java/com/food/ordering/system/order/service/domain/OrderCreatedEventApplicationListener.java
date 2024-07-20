package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.IOrderCreatedPaymentRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedEventApplicationListener {
    private final IOrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    @TransactionalEventListener
    void process(OrderCreatedEvent orderCreatedEvent) {

    }
}
