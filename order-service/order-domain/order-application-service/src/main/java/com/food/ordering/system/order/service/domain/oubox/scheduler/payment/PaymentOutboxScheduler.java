package com.food.ordering.system.order.service.domain.oubox.scheduler.payment;

import com.food.ordering.system.order.service.domain.oubox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.IPaymentRequestMessagePublisher;
import com.food.ordering.system.outbox.IOutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOutboxScheduler implements IOutboxScheduler {

    private final PaymentOutboxHelper paymentOutboxHelper;
    private final IPaymentRequestMessagePublisher paymentRequestMessagePublisher;

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}", initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        Optional<List<OrderPaymentOutboxMessage>> outboxMessagesResponse =
                paymentOutboxHelper.getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.Started,
                        SagaStatus.Started,
                        SagaStatus.Compensating
                );

        if (outboxMessagesResponse.isPresent() && !outboxMessagesResponse.get().isEmpty()) {
            List<OrderPaymentOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            if (log.isInfoEnabled()) {
                log.info("Received [{}] OrderPaymentOutboxMessage with ids: [{}], sending to message bus!",
                        outboxMessages.size(),
                        outboxMessages.stream()
                                .map(OrderPaymentOutboxMessage::getId)
                                .collect(Collectors.joining(","))
                );
            }
            outboxMessages.forEach(
                    outboxMessage -> paymentRequestMessagePublisher.publish(outboxMessage, this::updateOutboxStatus)
            );
            if (log.isInfoEnabled()) {
                log.info("[{}] OrderPaymentOutboxMessage sent to message bus!", outboxMessages.size());
            }
        }

    }

    private void updateOutboxStatus(OrderPaymentOutboxMessage orderPaymentOutboxMessage, OutboxStatus outboxStatus) {
        orderPaymentOutboxMessage.setOutboxStatus(outboxStatus);
        paymentOutboxHelper.save(orderPaymentOutboxMessage);
        if (log.isInfoEnabled()) {
            log.info("OrderPaymentOutboxMessage is updated with outbox status: [{}]", outboxStatus.name());
        }
    }
}