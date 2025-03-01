package com.food.ordering.system.order.service.domain.oubox.scheduler.payment;

import com.food.ordering.system.order.service.domain.oubox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.outbox.IOutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PaymentOutboxCleanerScheduler implements IOutboxScheduler {

    private final PaymentOutboxHelper paymentOutboxHelper;

    public PaymentOutboxCleanerScheduler(PaymentOutboxHelper paymentOutboxHelper) {
        this.paymentOutboxHelper = paymentOutboxHelper;
    }

    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        Optional<List<OrderPaymentOutboxMessage>> outboxMessagesResponse =
                paymentOutboxHelper.getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.Completed,
                        SagaStatus.Succeeded,
                        SagaStatus.Failed,
                        SagaStatus.Compensated
                );

        if (outboxMessagesResponse.isPresent()) {
            List<OrderPaymentOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            if (log.isInfoEnabled()) {
                log.info("Received [{}] OrderPaymentOutboxMessage for clean-up. The payloads: [{}]",
                        outboxMessages.size(),
                        outboxMessages.stream().map(OrderPaymentOutboxMessage::getPayload)
                                .collect(Collectors.joining("\n")));
            }
            paymentOutboxHelper.deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(
                    OutboxStatus.Completed,
                    SagaStatus.Succeeded,
                    SagaStatus.Failed,
                    SagaStatus.Compensated
            );
            if (log.isInfoEnabled()) {
                log.info("[{}] OrderPaymentOutboxMessage deleted!", outboxMessages.size());
            }
        }

    }
}