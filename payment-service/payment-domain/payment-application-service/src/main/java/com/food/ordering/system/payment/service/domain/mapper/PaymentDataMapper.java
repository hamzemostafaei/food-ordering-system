package com.food.ordering.system.payment.service.domain.mapper;

import com.food.ordering.system.domain.value.object.CustomerId;
import com.food.ordering.system.domain.value.object.Money;
import com.food.ordering.system.domain.value.object.OrderId;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataMapper {
    public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(new OrderId(paymentRequest.getOrderId()))
                .customerId(new CustomerId(paymentRequest.getCustomerId()))
                .price(new Money(paymentRequest.getPrice()))
                .build();
    }

    public OrderEventPayload paymentEventToOrderEventPayload(PaymentEvent paymentEvent) {
        return OrderEventPayload.builder()
                .paymentId(paymentEvent.getPayment().getId().getValue())
                .customerId(paymentEvent.getPayment().getCustomerId().getValue())
                .orderId(paymentEvent.getPayment().getOrderId().getValue())
                .price(paymentEvent.getPayment().getPrice().getAmount())
                .createdAt(paymentEvent.getCreatedAt())
                .paymentStatus(paymentEvent.getPayment().getPaymentStatus().name())
                .failureMessages(paymentEvent.getFailureMessages())
                .build();
    }
}
