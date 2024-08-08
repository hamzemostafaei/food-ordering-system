package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.IPaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
public class PaymentResponseMessageListenerImpl implements IPaymentResponseMessageListener {

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {

    }

    @Override
    public void PaymentCanceled(PaymentResponse paymentResponse) {

    }
}