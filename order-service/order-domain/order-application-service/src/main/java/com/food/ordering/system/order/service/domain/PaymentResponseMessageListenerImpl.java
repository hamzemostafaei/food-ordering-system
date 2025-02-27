package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.IPaymentResponseMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.food.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class PaymentResponseMessageListenerImpl implements IPaymentResponseMessageListener {

    private final OrderPaymentSaga orderPaymentSaga;

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        orderPaymentSaga.process(paymentResponse);
        if (log.isInfoEnabled()) {
            log.info("Order Payment Saga process operation is completed for order id: [{}]", paymentResponse.getOrderId());
        }
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        if (log.isInfoEnabled()) {
            log.info("Order is rolled back for order id: [{}] with failure messages: [{}]",
                    paymentResponse.getOrderId(),
                    String.join(FAILURE_MESSAGE_DELIMITER, paymentResponse.getFailureMessages()));
        }
    }
}
