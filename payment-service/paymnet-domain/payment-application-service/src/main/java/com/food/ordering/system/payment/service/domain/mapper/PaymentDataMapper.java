package com.food.ordering.system.payment.service.domain.mapper;

import com.food.ordering.system.domain.value.object.CustomerId;
import com.food.ordering.system.domain.value.object.Money;
import com.food.ordering.system.domain.value.object.OrderId;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.Payment;
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
}
