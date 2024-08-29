package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.IPaymentRequestMessageListener;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.IPaymentCancelledMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.IPaymentCompletedMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.IPaymentFailedMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRequestMessageListenerImpl implements IPaymentRequestMessageListener {

    private final PaymentRequestHelper paymentRequestHelper;
    private final IPaymentCompletedMessagePublisher paymentCompletedMessagePublisher;
    private final IPaymentCancelledMessagePublisher paymentCancelledMessagePublisher;
    private final IPaymentFailedMessagePublisher paymentFailedMessagePublisher;

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequest);
        fireEvent(paymentEvent);
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        PaymentEvent paymentEvent = paymentRequestHelper.persistCancelPayment(paymentRequest);
        fireEvent(paymentEvent);
    }

    private void fireEvent(PaymentEvent paymentEvent) {
        log.info("Publishing payment event with payment id: {} and order id: {}",
                paymentEvent.getPayment().getId().getValue(),
                paymentEvent.getPayment().getOrderId().getValue());

        if (paymentEvent instanceof PaymentCompletedEvent paymentCompletedEvent) {
            paymentCompletedMessagePublisher.publish(paymentCompletedEvent);
        } else if (paymentEvent instanceof PaymentCancelledEvent paymentCancelledEvent) {
            paymentCancelledMessagePublisher.publish(paymentCancelledEvent);
        } else {
            PaymentFailedEvent paymentFailedEvent = (PaymentFailedEvent) paymentEvent;
        }
    }
}
